package app.habitzl.elasticsearch.status.monitor.tool.data.connection;

/**
 * All possible connection states.
 */
public enum ConnectionStatus {

	/**
	 * The tool was able to connect to the Elasticsearch cluster.
	 */
	SUCCESS,

	/**
	 * The tool could not find the endpoint of the Elasticsearch cluster.
	 */
	NOT_FOUND,

	/**
	 * The tool and the Elasticsearch cluster could not negotiate the desired level of security.
	 */
	SSL_HANDSHAKE_FAILURE,
}
