package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import org.elasticsearch.client.RestClient;

/**
 * A factory for creating Elasticsearch REST clients.
 */
public interface RestClientFactory {

    /**
     * Creates a new Elasticsearch REST client.
     */
    RestClient create();
}
