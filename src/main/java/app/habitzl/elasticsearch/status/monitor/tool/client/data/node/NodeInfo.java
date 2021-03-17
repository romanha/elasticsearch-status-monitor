package app.habitzl.elasticsearch.status.monitor.tool.client.data.node;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public final class NodeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nodeId;
    private final String nodeName;
    private final String processId;
    private final String jvmVersion;
    private final String elasticsearchVersion;
    private final boolean isMasterNode;
    private final boolean isMasterEligibleNode;
    private final boolean isDataNode;
    private final boolean isIngestNode;
    private final EndpointInfo endpointInfo;
    private final NodeStats nodeStats;

    public NodeInfo(
            final String nodeId,
            final String nodeName,
            final String processId,
            final String jvmVersion,
            final String elasticsearchVersion,
            final boolean isMasterNode,
            final boolean isMasterEligibleNode,
            final boolean isDataNode,
            final boolean isIngestNode,
            final EndpointInfo endpointInfo,
            final NodeStats nodeStats) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.processId = processId;
        this.jvmVersion = jvmVersion;
        this.elasticsearchVersion = elasticsearchVersion;
        this.isMasterNode = isMasterNode;
        this.isMasterEligibleNode = isMasterEligibleNode;
        this.isDataNode = isDataNode;
        this.isIngestNode = isIngestNode;
        this.endpointInfo = endpointInfo;
        this.nodeStats = nodeStats;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getProcessId() {
        return processId;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public String getElasticsearchVersion() {
        return elasticsearchVersion;
    }

    public boolean isMasterNode() {
        return isMasterNode;
    }

    public boolean isMasterEligibleNode() {
        return isMasterEligibleNode;
    }

    public boolean isDataNode() {
        return isDataNode;
    }

    public boolean isIngestNode() {
        return isIngestNode;
    }

    public EndpointInfo getEndpointInfo() {
        return endpointInfo;
    }

    public NodeStats getNodeStats() {
        return nodeStats;
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
                    && Objects.equals(isMasterEligibleNode, nodeInfo.isMasterEligibleNode)
                    && Objects.equals(isDataNode, nodeInfo.isDataNode)
                    && Objects.equals(isIngestNode, nodeInfo.isIngestNode)
                    && Objects.equals(nodeId, nodeInfo.nodeId)
                    && Objects.equals(nodeName, nodeInfo.nodeName)
                    && Objects.equals(processId, nodeInfo.processId)
                    && Objects.equals(jvmVersion, nodeInfo.jvmVersion)
                    && Objects.equals(elasticsearchVersion, nodeInfo.elasticsearchVersion)
                    && Objects.equals(endpointInfo, nodeInfo.endpointInfo)
                    && Objects.equals(nodeStats, nodeInfo.nodeStats);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                nodeId,
                nodeName,
                processId,
                jvmVersion,
                elasticsearchVersion,
                isMasterNode,
                isMasterEligibleNode,
                isDataNode,
                isIngestNode,
                endpointInfo,
                nodeStats
        );
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NodeInfo.class.getSimpleName() + "[", "]")
                .add("nodeId='" + nodeId + "'")
                .add("nodeName='" + nodeName + "'")
                .add("processId='" + processId + "'")
                .add("jvmVersion='" + jvmVersion + "'")
                .add("elasticsearchVersion='" + elasticsearchVersion + "'")
                .add("isMasterNode=" + isMasterNode)
                .add("isMasterEligibleNode=" + isMasterEligibleNode)
                .add("isDataNode=" + isDataNode)
                .add("isIngestNode=" + isIngestNode)
                .add("endpointInfo=" + endpointInfo)
                .add("nodeStats=" + nodeStats)
                .toString();
    }
}
