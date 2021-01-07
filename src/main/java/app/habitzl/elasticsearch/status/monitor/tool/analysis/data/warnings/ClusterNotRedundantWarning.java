/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;

import java.util.Objects;

public class ClusterNotRedundantWarning implements Warning {
    private static final long serialVersionUID = 1L;

    private final boolean notEnoughMasterEligibleNodes;
    private final boolean notEnoughDataNodes;
    private final boolean quorumIsSetToMaximumMasterEligibleNodes;

    public static ClusterNotRedundantWarning create(
            final boolean notEnoughMasterEligibleNodes,
            final boolean notEnoughDataNodes,
            final boolean quorumIsSetToMaximumMasterEligibleNodes) {
        return new ClusterNotRedundantWarning(notEnoughMasterEligibleNodes, notEnoughDataNodes, quorumIsSetToMaximumMasterEligibleNodes);
    }

    private ClusterNotRedundantWarning(
            final boolean notEnoughMasterEligibleNodes,
            final boolean notEnoughDataNodes,
            final boolean quorumIsSetToMaximumMasterEligibleNodes) {
        this.notEnoughMasterEligibleNodes = notEnoughMasterEligibleNodes;
        this.notEnoughDataNodes = notEnoughDataNodes;
        this.quorumIsSetToMaximumMasterEligibleNodes = quorumIsSetToMaximumMasterEligibleNodes;
    }

    @Override
    public String getTitle() {
        return "Cluster is not redundant";
    }

    @Override
    public String getDescription() {
        return "The cluster is not set up in a redundant way. An endpoint or node outage can result in a cluster failure or data loss.";
    }

    @Override
    public String getSolution() {
        return "Start at least 2 master-eligible nodes on different endpoints. Start at least 2 data nodes on different endpoints.";
    }

    @Override
    public String getAdditionalInformation() {
        StringBuilder builder = new StringBuilder("");
        if (notEnoughMasterEligibleNodes) {
            builder.append("The cluster does not have enough master-eligible nodes running on different endpoints. ");
        }

        if (notEnoughDataNodes) {
            builder.append("The cluster does not have enough data nodes running on different endpoints. ");
        }

        if (quorumIsSetToMaximumMasterEligibleNodes) {
            builder.append("The setting 'discovery.zen.minimum_master_nodes' should not be set to the maximum number of master-eligible nodes. ");
        }

        return builder.toString();
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
            ClusterNotRedundantWarning warning = (ClusterNotRedundantWarning) o;
            isEqual = Objects.equals(notEnoughMasterEligibleNodes, warning.notEnoughMasterEligibleNodes)
                    && Objects.equals(notEnoughDataNodes, warning.notEnoughDataNodes)
                    && Objects.equals(quorumIsSetToMaximumMasterEligibleNodes, warning.quorumIsSetToMaximumMasterEligibleNodes);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(notEnoughMasterEligibleNodes, notEnoughDataNodes, quorumIsSetToMaximumMasterEligibleNodes);
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
