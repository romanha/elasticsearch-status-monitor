package app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster;

public enum ClusterHealthStatus {

	/**
	 * The cluster is perfectly fine, all shards have been allocated.
	 */
	GREEN,

	/**
	 * The cluster is functional, at least all primary shards have been allocated.
	 */
	YELLOW,

	/**
	 * The cluster is not functional, not all primary shards have been allocated.
	 */
	RED,

	/**
	 * There is no information about the cluster.
	 */
	UNKNOWN
}
