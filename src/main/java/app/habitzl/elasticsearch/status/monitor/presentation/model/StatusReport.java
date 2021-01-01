package app.habitzl.elasticsearch.status.monitor.presentation.model;

import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The status report model.
 */
@Immutable
public final class StatusReport implements Serializable {
	private static final long serialVersionUID = 1L;

	private final ConnectionStatus connectionStatus;
	private final ClusterInfo clusterInfo;
	private final List<NodeInfo> nodeInfos;

	public static StatusReport error(final ConnectionStatus status) {
		return new StatusReport(status, null, List.of());
	}

	public static StatusReport create(
			final ClusterInfo cluster,
			final List<NodeInfo> nodes) {
		List<NodeInfo> sortedNodes = sortNodesByName(nodes);
		return new StatusReport(ConnectionStatus.SUCCESS, cluster, sortedNodes);
	}

	private static List<NodeInfo> sortNodesByName(final List<NodeInfo> nodes) {
		List<NodeInfo> sortedNodes = new ArrayList<>(nodes);
		sortedNodes.sort(Comparator.comparing(NodeInfo::getNodeName));
		return sortedNodes;
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

	@Override
	@SuppressWarnings("CyclomaticComplexity")
	public boolean equals(final Object o) {
		boolean isEqual;

		if (this == o) {
			isEqual = true;
		} else if (o == null || getClass() != o.getClass()) {
			isEqual = false;
		} else {
			StatusReport report = (StatusReport) o;
			isEqual = Objects.equals(connectionStatus, report.connectionStatus)
					&& Objects.equals(clusterInfo, report.clusterInfo)
					&& Objects.equals(nodeInfos, report.nodeInfos);
		}

		return isEqual;
	}

	@Override
	public int hashCode() {
		return Objects.hash(connectionStatus, clusterInfo, nodeInfos);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", StatusReport.class.getSimpleName() + "[", "]")
				.add("connectionStatus=" + connectionStatus)
				.add("clusterInfo=" + clusterInfo)
				.add("nodeInfos=" + nodeInfos)
				.toString();
	}
}
