/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;

import java.util.Objects;

public class SplitBrainPossibleWarning implements Warning {
    private static final long serialVersionUID = 1L;

    private final int configuredQuorum;
    private final int requiredQuorum;
    private final int numberOfMasterEligibleNodes;

    public static SplitBrainPossibleWarning create(final int configuredQuorum, final int requiredQuorum, final int numberOfMasterEligibleNodes) {
        return new SplitBrainPossibleWarning(configuredQuorum, requiredQuorum, numberOfMasterEligibleNodes);
    }

    private SplitBrainPossibleWarning(final int configuredQuorum, final int requiredQuorum, final int numberOfMasterEligibleNodes) {
        this.configuredQuorum = configuredQuorum;
        this.requiredQuorum = requiredQuorum;
        this.numberOfMasterEligibleNodes = numberOfMasterEligibleNodes;
    }

    @Override
    public String getTitle() {
        return "Split brain scenario is possible";
    }

    @Override
    public String getDescription() {
        return "The cluster can run into a split brain scenario with the current setup. This can cause data loss and an inconsistent search behaviour.";
    }

    @Override
    public String getSolution() {
        return "The setting 'discovery.zen.minimum_master_nodes' should be set to a quorum of master-eligible nodes: (master-eligible nodes / 2) + 1";
    }

    @Override
    public String getAdditionalInformation() {
        return String.format(
                "The cluster has %d master-eligible nodes. This requires a quorum of %d. The configured quorum is %d.",
                numberOfMasterEligibleNodes,
                requiredQuorum,
                configuredQuorum
        );
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
            SplitBrainPossibleWarning warning = (SplitBrainPossibleWarning) o;
            isEqual = Objects.equals(configuredQuorum, warning.configuredQuorum)
                    && Objects.equals(requiredQuorum, warning.requiredQuorum)
                    && Objects.equals(numberOfMasterEligibleNodes, warning.numberOfMasterEligibleNodes);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(configuredQuorum, requiredQuorum, numberOfMasterEligibleNodes);
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
