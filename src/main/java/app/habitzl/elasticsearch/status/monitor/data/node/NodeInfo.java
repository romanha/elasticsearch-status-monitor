package app.habitzl.elasticsearch.status.monitor.data.node;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.StringJoiner;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
@Immutable
public class NodeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String nodeName;
	private final boolean isMasterNode;
	private final boolean isDataNode;
	private final boolean isMasterEligibleNode;
	private final float loadAverageLast15Minutes;
	private final EndpointInfo endpointInfo;

	public NodeInfo(
			final String nodeName,
			final boolean isMasterNode,
			final boolean isDataNode,
			final boolean isMasterEligibleNode,
			final float loadAverageLast15Minutes,
			final EndpointInfo endpointInfo) {
		this.nodeName = nodeName;
		this.isMasterNode = isMasterNode;
		this.isDataNode = isDataNode;
		this.isMasterEligibleNode = isMasterEligibleNode;
		this.loadAverageLast15Minutes = loadAverageLast15Minutes;
		this.endpointInfo = endpointInfo;
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

	public float getLoadAverageLast15Minutes() {
		return loadAverageLast15Minutes;
	}

	public EndpointInfo getEndpointInfo() {
		return endpointInfo;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", NodeInfo.class.getSimpleName() + "[", "]")
				.add("nodeName='" + nodeName + "'")
				.add("isMasterNode=" + isMasterNode)
				.add("isDataNode=" + isDataNode)
				.add("isMasterEligibleNode=" + isMasterEligibleNode)
				.add("loadAverageLast15Minutes=" + loadAverageLast15Minutes)
				.add("endpointInfo=" + endpointInfo)
				.toString();
	}
}
