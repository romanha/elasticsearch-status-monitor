package app.habitzl.elasticsearch.status.monitor.tool.client.data.shard;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public class NodeAllocationDecision {

    private final String nodeId;
    private final String nodeName;
    private final List<String> decisions;

    public NodeAllocationDecision(final String nodeId, final String nodeName, final List<String> decisions) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.decisions = Objects.isNull(decisions) ? List.of() : List.copyOf(decisions);
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public List<String> getDecisions() {
        return decisions;
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
            NodeAllocationDecision that = (NodeAllocationDecision) o;
            isEqual = Objects.equals(nodeId, that.nodeId)
                    && Objects.equals(nodeName, that.nodeName)
                    && Objects.equals(decisions, that.decisions);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, nodeName, decisions);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NodeAllocationDecision.class.getSimpleName() + "[", "]")
                .add("nodeId='" + nodeId + "'")
                .add("nodeName='" + nodeName + "'")
                .add("decisions=" + decisions)
                .toString();
    }
}
