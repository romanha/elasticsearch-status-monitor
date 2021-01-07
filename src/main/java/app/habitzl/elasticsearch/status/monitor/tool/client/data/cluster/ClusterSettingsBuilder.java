/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster;

public final class ClusterSettingsBuilder {
    private int minimumOfRequiredMasterNodesForElection = ClusterSettings.ES_DEFAULT_MINIMUM_REQUIRED_MASTER_NODES_FOR_ELECTION;

    private ClusterSettingsBuilder() {
    }

    static ClusterSettingsBuilder create() {
        return new ClusterSettingsBuilder();
    }

    public ClusterSettingsBuilder withMinimumOfRequiredMasterNodesForElection(int minimumOfRequiredMasterNodesForElection) {
        this.minimumOfRequiredMasterNodesForElection = minimumOfRequiredMasterNodesForElection;
        return this;
    }

    public ClusterSettings build() {
        return new ClusterSettings(minimumOfRequiredMasterNodesForElection);
    }
}
