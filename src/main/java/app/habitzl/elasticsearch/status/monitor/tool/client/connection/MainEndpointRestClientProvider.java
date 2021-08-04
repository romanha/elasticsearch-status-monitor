package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import com.google.inject.Provider;
import org.elasticsearch.client.RestClient;

import javax.inject.Inject;
import java.util.Objects;

/**
 * A provider responsible for returning a single instance of an Elasticsearch REST client.
 */
public class MainEndpointRestClientProvider implements Provider<RestClient> {

    private RestClient client;
    private final RestClientFactory factory;

    @Inject
    public MainEndpointRestClientProvider(final RestClientFactory factory) {
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
