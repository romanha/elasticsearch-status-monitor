package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FallbackElasticsearchRestClientFactory
        extends RestClientFactoryWithSecuritySupport
        implements FallbackRestClientFactory {
    private static final Logger LOG = LogManager.getLogger(FallbackElasticsearchRestClientFactory.class);

    static final String HOST_PORT_SEPARATOR = ":";
    static final String HTTP_SCHEME = "http";
    static final String HTTPS_SCHEME = "https";

    private final StatusMonitorConfiguration configuration;

    @Inject
    public FallbackElasticsearchRestClientFactory(final StatusMonitorConfiguration configuration) {
        super(configuration);
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
