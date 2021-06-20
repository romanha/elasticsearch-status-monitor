package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FallbackElasticsearchRestClientFactory implements FallbackRestClientFactory {
    private static final Logger LOG = LogManager.getLogger(FallbackElasticsearchRestClientFactory.class);

    static final String HOST_PORT_SEPARATOR = ":";
    static final String HTTP_SCHEME = "http";
    static final String HTTPS_SCHEME = "https";

    private static final String DEFAULT_TRUST_STORE_PASSWORD = "changeit";

    private final StatusMonitorConfiguration configuration;

    @Inject
    public FallbackElasticsearchRestClientFactory(final StatusMonitorConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public List<RestClient> create() {
        List<String> endpoints = configuration.getFallbackEndpoints();
        return endpoints.stream()
                .map(this::buildClient)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<RestClient> buildClient(final String endpoint) {
        RestClient client = null;
        String[] hostAndPort = endpoint.split(HOST_PORT_SEPARATOR);
        if (hostAndPort.length == 2 && isInteger(hostAndPort[1])) {
            Optional<HttpHost> httpHost = createHttpHost(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
            if (httpHost.isPresent()) {
                RestClientBuilder builder = RestClient.builder(httpHost.get());
                builder.setHttpClientConfigCallback(createHttpClientConfigCallback());
                client = builder.build();
            }
        } else {
            LOG.warn("Failed to parse host and port part from fallback endpoint '{}'. Ignoring endpoint.", endpoint);
        }

        return Optional.ofNullable(client);
    }

    private Optional<HttpHost> createHttpHost(final String givenHost, final int givenPort) {
        HttpHost host = null;

        String scheme = configuration.isUsingHttps() ? HTTPS_SCHEME : HTTP_SCHEME;

        try {
            InetAddress address = InetAddress.getByName(givenHost);
            host = new HttpHost(address, givenPort, scheme);
        } catch (final UnknownHostException e) {
            LOG.warn("Failed to create host from '" + givenHost + "'. Ignoring endpoint.", e);
        }

        return Optional.ofNullable(host);
    }

    private RestClientBuilder.HttpClientConfigCallback createHttpClientConfigCallback() {
        HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();

        if (configuration.isUsingHttps()) {
            setCredentialsProvider(builder);
            setDefaultSSLContext(builder);
//            setCustomSSLContext(builder);
        }

        return callback -> builder;
    }

    private void setCredentialsProvider(final HttpAsyncClientBuilder builder) {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                configuration.getUsername(),
                configuration.getPassword()
        );
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        builder.setDefaultCredentialsProvider(credentialsProvider);
    }

    /**
     * Set the SSL context based on the standard JSSE trust material (from {@code %JAVA_HOME%/lib/security/cacerts}).
     * System properties are not taken into consideration.
     */
    private void setDefaultSSLContext(final HttpAsyncClientBuilder builder) {
        SSLContext sslContext = SSLContexts.createDefault();
        builder.setSSLContext(sslContext);
    }

    /**
     * Set the SSL context based on a custom trust material.
     * For now, this also takes the default Java trust store ({@code %JAVA_HOME%/lib/security/cacerts}).
     */
    @SuppressWarnings("unused")
    private void setCustomSSLContext(final HttpAsyncClientBuilder builder) {
        Optional<SSLContext> sslContext = getCustomSslContext();
        sslContext.ifPresent(builder::setSSLContext);
    }

    /**
     * Get the SSL context from the default Java trust store ({@code %JAVA_HOME%/lib/security/cacerts}).
     */
    private Optional<SSLContext> getCustomSslContext() {
        SSLContext sslContext = null;
        try {
            String trustStoreType = KeyStore.getDefaultType();
            KeyStore trustStore = KeyStore.getInstance(trustStoreType);

            Path trustStorePath = getDefaultTrustStorePath();
            try (InputStream file = Files.newInputStream(trustStorePath)) {
                trustStore.load(file, DEFAULT_TRUST_STORE_PASSWORD.toCharArray());
                LOG.info("Loaded the Java trust store from the path '{}'.", trustStorePath);
            }

            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, null)
                    .build();
        } catch (final GeneralSecurityException | IOException e) {
            LOG.error("Failed to create SSL context.", e);
        }

        return Optional.ofNullable(sslContext);
    }

    /**
     * Gets the default trust store path located at {@code %JAVA_HOME%/lib/security/cacerts}.
     */
    private Path getDefaultTrustStorePath() {
        return Paths.get(
                System.getProperties().getProperty("java.home") + File.separator
                        + "lib" + File.separator
                        + "security" + File.separator
                        + "cacerts"
        );
    }

    private boolean isInteger(final String port) {
        boolean result = true;
        try {
            Integer.parseInt(port);
        } catch (final NumberFormatException e) {
            result = false;
        }

        return result;
    }
}
