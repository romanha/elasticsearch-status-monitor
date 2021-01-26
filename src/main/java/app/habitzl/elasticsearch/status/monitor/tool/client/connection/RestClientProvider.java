package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import org.elasticsearch.client.RestClient;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Objects;

/**
 * A provider responsible for returning a single instance of an Elasticsearch high-level REST client.
 */
public class RestClientProvider implements Provider<RestClient> {

    private RestClient client;
    private final RestClientFactory factory;

    @Inject
    public RestClientProvider(final RestClientFactory factory) {
        this.factory = factory;
    }

    @Override
    public RestClient get() {
        return Objects.nonNull(client) ? client : createClient();
    }

    private RestClient createClient() {
        client = factory.create();
        return client;
    }
}
