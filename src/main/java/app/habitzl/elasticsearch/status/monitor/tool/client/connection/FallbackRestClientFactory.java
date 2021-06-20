package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import org.elasticsearch.client.RestClient;

import java.util.List;

/**
 * A factory for creating Elasticsearch REST clients that act as fallback clients in case the main endpoint fails to connect.
 */
public interface FallbackRestClientFactory {

    /**
     * Creates a list of new Elasticsearch REST clients.
     */
    List<RestClient> create();
}
