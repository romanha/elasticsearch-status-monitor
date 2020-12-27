package app.habitzl.elasticsearch.status.monitor.tool.data.node;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public final class NodeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String processId;
	private final String nodeName;
	private final boolean isMasterNode;
	private final boolean isDataNode;
	private final boolean isMasterEligibleNode;
	private final String uptime;
	private final float loadAverageLast15Minutes;
	private final EndpointInfo endpointInfo;

	public NodeInfo(
			final String processId,
			final String nodeName,
			final boolean isMasterNode,
			final boolean isDataNode,
			final boolean isMasterEligibleNode,
			final String uptime,
			final float loadAverageLast15Minutes,
			final EndpointInfo endpointInfo) {
		this.processId = processId;
		this.nodeName = nodeName;
		this.isMasterNode = isMasterNode;
		this.isDataNode = isDataNode;
		this.isMasterEligibleNode = isMasterEligibleNode;
		this.uptime = uptime;
		this.loadAverageLast15Minutes = loadAverageLast15Minutes;
		this.endpointInfo = endpointInfo;
	}

	public String getProcessId() {
		return processId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public boolean isMasterNode() {
		return isMasterNode;
	}

	public boolean isDataNode() {
		return isDataNode;
	}

	public boolean isMasterEligibleNode() {
		return isMasterEligibleNode;
	}

	public String getUptime() {
		return uptime;
	}

	public float getLoadAverageLast15Minutes() {
		return loadAverageLast15Minutes;
	}

	public EndpointInfo getEndpointInfo() {
		return endpointInfo;
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
			NodeInfo nodeInfo = (NodeInfo) o;
			isEqual = Objects.equals(isMasterNode, nodeInfo.isMasterNode)
					&& Objects.equals(isDataNode, nodeInfo.isDataNode)
					&& Objects.equals(isMasterEligibleNode, nodeInfo.isMasterEligibleNode)
					&& Objects.equals(loadAverageLast15Minutes, nodeInfo.loadAverageLast15Minutes)
					&& Objects.equals(processId, nodeInfo.processId)
					&& Objects.equals(nodeName, nodeInfo.nodeName)
					&& Objects.equals(uptime, nodeInfo.uptime)
					&& Objects.equals(endpointInfo, nodeInfo.endpointInfo);
		}

		return isEqual;
	}

	@Override
	public int hashCode() {
		return Objects.hash(processId, nodeName, isMasterNode, isDataNode, isMasterEligibleNode, uptime, loadAverageLast15Minutes, endpointInfo);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", NodeInfo.class.getSimpleName() + "[", "]")
				.add("processId='" + processId + "'")
				.add("nodeName='" + nodeName + "'")
				.add("isMasterNode=" + isMasterNode)
				.add("isDataNode=" + isDataNode)
				.add("isMasterEligibleNode=" + isMasterEligibleNode)
				.add("uptime='" + uptime + "'")
				.add("loadAverageLast15Minutes=" + loadAverageLast15Minutes)
				.add("endpointInfo=" + endpointInfo)
				.toString();
	}
}
