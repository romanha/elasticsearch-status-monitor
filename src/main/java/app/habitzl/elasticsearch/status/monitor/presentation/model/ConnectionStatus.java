package app.habitzl.elasticsearch.status.monitor.presentation.model;

/**
 * All possible connection states.
 */
public enum ConnectionStatus {

	/**
	 * The tool was able to connect to the Elasticsearch cluster.
	 */
	SUCCESS,

	/**
	 * The tool was not able to connect to the Elasticsearch cluster.
	 */
	UNABLE_TO_CONNECT,
}
