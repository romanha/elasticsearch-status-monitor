package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import org.elasticsearch.client.RestClient;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Objects;

/**
 * A provider responsible for returning a list of Elasticsearch REST clients.
 */
public class FallbackEndpointRestClientProvider implements Provider<List<RestClient>> {

    private List<RestClient> clients;
    private final FallbackRestClientFactory factory;

    @Inject
    public FallbackEndpointRestClientProvider(final FallbackRestClientFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<RestClient> get() {
        return Objects.nonNull(clients) ? clients : createClients();
    }

    private List<RestClient> createClients() {
        clients = factory.create();
        return clients;
    }
}
