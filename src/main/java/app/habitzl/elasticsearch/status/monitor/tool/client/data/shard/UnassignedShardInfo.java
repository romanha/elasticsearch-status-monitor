package app.habitzl.elasticsearch.status.monitor.tool.client.data.shard;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public class UnassignedShardInfo {

    private final String indexName;
    private final int shardNumber;
    private final boolean isPrimaryShard;
    private final UnassignedReason unassignedReason;
    private final Instant unassignedSince;
    private final String explanation;
    private final List<NodeAllocationDecision> nodeDecisions;

    public UnassignedShardInfo(
            final String indexName,
            final int shardNumber,
            final boolean isPrimaryShard,
            final UnassignedReason unassignedReason,
            final Instant unassignedSince,
            final String explanation,
            final List<NodeAllocationDecision> nodeDecisions) {
        this.indexName = indexName;
        this.shardNumber = shardNumber;
        this.isPrimaryShard = isPrimaryShard;
        this.unassignedReason = Objects.isNull(unassignedReason) ? UnassignedReason.UNKNOWN_REASON : unassignedReason;
        this.unassignedSince = unassignedSince;
        this.explanation = explanation;
        this.nodeDecisions = Objects.isNull(nodeDecisions) ? List.of() : List.copyOf(nodeDecisions);
    }

    public String getIndexName() {
        return indexName;
    }

    public int getShardNumber() {
        return shardNumber;
    }

    public boolean isPrimaryShard() {
        return isPrimaryShard;
    }

    public UnassignedReason getUnassignedReason() {
        return unassignedReason;
    }

    public Instant getUnassignedSince() {
        return unassignedSince;
    }

    public String getExplanation() {
        return explanation;
    }

    public List<NodeAllocationDecision> getNodeDecisions() {
        return nodeDecisions;
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
            UnassignedShardInfo that = (UnassignedShardInfo) o;
            isEqual = Objects.equals(shardNumber, that.shardNumber)
                    && Objects.equals(isPrimaryShard, that.isPrimaryShard)
                    && Objects.equals(indexName, that.indexName)
                    && Objects.equals(unassignedReason, that.unassignedReason)
                    && Objects.equals(unassignedSince, that.unassignedSince)
                    && Objects.equals(explanation, that.explanation)
                    && Objects.equals(nodeDecisions, that.nodeDecisions);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(indexName, shardNumber, isPrimaryShard, unassignedReason, unassignedSince, explanation, nodeDecisions);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UnassignedShardInfo.class.getSimpleName() + "[", "]")
                .add("indexName='" + indexName + "'")
                .add("shardNumber=" + shardNumber)
                .add("isPrimaryShard=" + isPrimaryShard)
                .add("unassignedReason=" + unassignedReason)
                .add("unassignedSince=" + unassignedSince)
                .add("explanation='" + explanation + "'")
                .add("nodeDecisions=" + nodeDecisions)
                .toString();
    }
}
