/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;

import java.util.Objects;
import java.util.stream.Collectors;

public class UnassignedShardsWarning implements Warning {
    private static final long serialVersionUID = 1L;

    private final UnassignedShardInfo info;

    public static UnassignedShardsWarning create(final UnassignedShardInfo unassignedShardInfo) {
        return new UnassignedShardsWarning(unassignedShardInfo);
    }

    private UnassignedShardsWarning(final UnassignedShardInfo info) {
        this.info = info;
    }

    @Override
    public String getTitle() {
        return "Unassigned shards";
    }

    @Override
    public String getDescription() {
        return "The cluster contains shards that are not assigned to any node. "
                + "This indicates that data is unavailable or that the cluster is not configured for the expected reliability.";
    }

    @Override
    public String getSolution() {
        return String.format("Explanation: '%s'.", info.getExplanation())
                + System.lineSeparator()
                + "Check the additional information why each node failed to allocate the shard.";
    }

    @Override
    public String getAdditionalInformation() {
        String mainInfo = String.format(
                "Shard %d of index '%s' is unassigned since %s.",
                info.getShardNumber(),
                info.getIndexName(),
                info.getUnassignedSinceString()
        );

        String reasonCode = String.format(
                "The last assignment was attempted because of the action '%s'.",
                info.getUnassignedReason().name()
        );

        String nodeDecisions =
                info.getNodeDecisions()
                    .stream()
                    .map(nodeDecision -> String.format(
                            "Allocation on node '%s' failed: '%s'",
                            nodeDecision.getNodeName(),
                            String.join(", ", nodeDecision.getDecisions())
                    ))
                    .collect(Collectors.joining(System.lineSeparator()));

        return mainInfo
                + System.lineSeparator()
                + reasonCode
                + System.lineSeparator()
                + System.lineSeparator()
                + nodeDecisions;
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
            UnassignedShardsWarning that = (UnassignedShardsWarning) o;
            isEqual = Objects.equals(info, that.info);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(info);
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
