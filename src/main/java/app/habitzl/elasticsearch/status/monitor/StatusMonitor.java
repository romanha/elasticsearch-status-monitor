package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

import java.util.List;

/**
 * The status monitor is capable of querying the Elasticsearch cluster for various metrics.
 */
public interface StatusMonitor {

	/**
	 * Gets information about the whole ES cluster.
	 */
	ClusterInfo getClusterInfo();

	/**
	 * Gets information about each node of the ES cluster.
	 */
	List<NodeInfo> getNodeInfo();
}
