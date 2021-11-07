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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ElasticsearchRestClientFactory
        extends RestClientFactoryWithSecuritySupport
        implements RestClientFactory {
    private static final Logger LOG = LogManager.getLogger(ElasticsearchRestClientFactory.class);

    static final String DEFAULT_HOST = "localhost";
    static final int DEFAULT_PORT = 9200;
    static final String HOST_PORT_SEPARATOR = ":";
    static final String HTTP_SCHEME = "http";
    static final String HTTPS_SCHEME = "https";

    private final StatusMonitorConfiguration configuration;

    @Inject
    public ElasticsearchRestClientFactory(final StatusMonitorConfiguration configuration) {
        super(configuration);
        this.configuration = configuration;
    }

    @Override
    public RestClient create() {
        RestClientBuilder builder = RestClient.builder(createHttpHosts().toArray(HttpHost[]::new));
        builder.setHttpClientConfigCallback(createHttpClientConfigCallback());
        return builder.build();
    }

    private List<HttpHost> createHttpHosts() {
        String scheme = configuration.isUsingHttps() ? HTTPS_SCHEME : HTTP_SCHEME;
        int port = getConfiguredPort();

        List<HttpHost> hosts = new ArrayList<>();

        createHttpHost(scheme, port).ifPresent(hosts::add);
        hosts.addAll(createFallbackHttpHosts(scheme));

        if (hosts.isEmpty()) {
            LOG.error(
                    "Failed to create any host from the main endpoint '{}' and the fallback endpoints '{}'.",
                    configuration.getHost(),
                    configuration.getFallbackEndpoints()
            );
            LOG.info("Falling back to the default endpoint '{}:{}'.", DEFAULT_HOST, port);
            hosts = List.of(createDefaultHost(scheme));
        }

        return hosts;
    }

    private Optional<HttpHost> createHttpHost(final String configuredScheme, final int configuredPort) {
        HttpHost host;

        try {
            InetAddress address = InetAddress.getByName(configuration.getHost());
            host = new HttpHost(address, configuredPort, configuredScheme);
        } catch (final UnknownHostException e) {
            LOG.error("Failed to create host from '" + configuration.getHost() + "'.", e);
            host = null;
        }

        return Optional.ofNullable(host);
    }

    private int getConfiguredPort() {
        int port;
        try {
            port = Integer.parseInt(configuration.getPort());
        } catch (final NumberFormatException e) {
            LOG.error("Failed to parse port from '" + configuration.getPort() + "'.", e);
            LOG.info("Falling back to '{}'.", DEFAULT_PORT);
            port = DEFAULT_PORT;
        }

        return port;
    }

    private List<HttpHost> createFallbackHttpHosts(final String scheme) {
        return configuration.getFallbackEndpoints()
                .stream()
                .map(endpoint -> createFallbackHttpHost(scheme, endpoint))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<HttpHost> createFallbackHttpHost(final String scheme, final String endpoint) {
        HttpHost host = null;

        try {
            String[] hostAndPort = endpoint.split(HOST_PORT_SEPARATOR);
            if (hostAndPort.length == 2) {
                String hostName = hostAndPort[0];
                int hostPort = Integer.parseInt(hostAndPort[1]);
                InetAddress address = InetAddress.getByName(hostName);
                host = new HttpHost(address, hostPort, scheme);
            } else {
                LOG.warn("Failed to parse host and port part from fallback endpoint '{}'. Ignoring endpoint.", endpoint);
            }
        } catch (final UnknownHostException e) {
            LOG.error("Failed to create fallback host from '" + endpoint + "'.", e);
        } catch (final NumberFormatException e) {
            LOG.warn("Failed to parse port part from fallback endpoint '{}'. Ignoring endpoint.", endpoint);
        }

        return Optional.ofNullable(host);
    }

    private HttpHost createDefaultHost(final String scheme) {
        return new HttpHost(DEFAULT_HOST, DEFAULT_PORT, scheme);
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
}
