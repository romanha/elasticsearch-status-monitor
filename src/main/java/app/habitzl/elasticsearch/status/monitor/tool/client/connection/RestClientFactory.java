package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import org.elasticsearch.client.RestHighLevelClient;

/**
 * A factory for creating Elasticsearch REST clients.
 */
public interface RestClientFactory {

	/**
	 * Creates a new high-level REST client.
	 */
	RestHighLevelClient create();
}
