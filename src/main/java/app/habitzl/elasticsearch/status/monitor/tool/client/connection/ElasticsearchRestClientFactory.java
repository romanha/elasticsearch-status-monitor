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

public class ElasticsearchRestClientFactory
        extends RestClientFactoryWithSecuritySupport
        implements RestClientFactory {
    private static final Logger LOG = LogManager.getLogger(ElasticsearchRestClientFactory.class);

    static final String FALLBACK_HOST = "localhost";
    static final int FALLBACK_PORT = 9200;
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
        RestClientBuilder builder = RestClient.builder(createHttpHost());
        builder.setHttpClientConfigCallback(createHttpClientConfigCallback());
        return builder.build();
    }

    private HttpHost createHttpHost() {
        HttpHost host;

        String scheme = configuration.isUsingHttps() ? HTTPS_SCHEME : HTTP_SCHEME;

        try {
            InetAddress address = InetAddress.getByName(configuration.getHost());
            host = new HttpHost(address, getConfiguredPort(), scheme);
        } catch (final UnknownHostException e) {
            LOG.error("Failed to create host from '" + configuration.getHost() + "'.", e);
            LOG.info("Falling back to '{}'.", FALLBACK_HOST);
            host = new HttpHost(FALLBACK_HOST, getConfiguredPort(), scheme);
        }

        return host;
    }

    private int getConfiguredPort() {
        int port;
        try {
            port = Integer.parseInt(configuration.getPort());
        } catch (final NumberFormatException e) {
            LOG.error("Failed to parse port from '" + configuration.getPort() + "'.", e);
            LOG.info("Falling back to '{}'.", FALLBACK_PORT);
            port = FALLBACK_PORT;
        }

        return port;
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
