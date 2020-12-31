package app.habitzl.elasticsearch.status.monitor.presentation.model;

import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

import java.util.List;

/**
 * The status report model.
 */
public class StatusReport {

	private final ConnectionStatus connectionStatus;
	private final ClusterInfo clusterInfo;
	private final List<NodeInfo> nodeInfos;

	public static StatusReport error(final ConnectionStatus status) {
		return new StatusReport(status, null, null);
	}

	public static StatusReport create(
			final ClusterInfo cluster,
			final List<NodeInfo> nodes) {
		return new StatusReport(ConnectionStatus.SUCCESS, cluster, nodes);
	}

	private StatusReport(
			final ConnectionStatus connectionStatus,
			final ClusterInfo clusterInfo,
			final List<NodeInfo> nodeInfos) {
		this.connectionStatus = connectionStatus;
		this.clusterInfo = clusterInfo;
		this.nodeInfos = nodeInfos;
	}

	public ConnectionStatus getConnectionStatus() {
		return connectionStatus;
	}

	public ClusterInfo getClusterInfo() {
		return clusterInfo;
	}

	public List<NodeInfo> getNodeInfos() {
		return nodeInfos;
	}
}
