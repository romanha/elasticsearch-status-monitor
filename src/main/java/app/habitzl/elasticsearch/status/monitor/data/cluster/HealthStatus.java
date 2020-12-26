package app.habitzl.elasticsearch.status.monitor.data.cluster;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public enum HealthStatus {

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
