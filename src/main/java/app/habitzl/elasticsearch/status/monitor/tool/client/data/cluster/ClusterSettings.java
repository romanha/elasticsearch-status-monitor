package app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public final class ClusterSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int ES_DEFAULT_MINIMUM_REQUIRED_MASTER_NODES_FOR_ELECTION = 1;

    private final int minimumOfRequiredMasterNodesForElection;

    // TODO create builder for this class and set defaults automatically if not defined by builder
    public static ClusterSettings createDefault() {
        return new ClusterSettings(ES_DEFAULT_MINIMUM_REQUIRED_MASTER_NODES_FOR_ELECTION);
    }

    public ClusterSettings(final int minimumOfRequiredMasterNodesForElection) {
        this.minimumOfRequiredMasterNodesForElection = minimumOfRequiredMasterNodesForElection;
    }

    public int getMinimumOfRequiredMasterNodesForElection() {
        return minimumOfRequiredMasterNodesForElection;
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
            ClusterSettings that = (ClusterSettings) o;
            isEqual = Objects.equals(minimumOfRequiredMasterNodesForElection, that.minimumOfRequiredMasterNodesForElection);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimumOfRequiredMasterNodesForElection);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ClusterSettings.class.getSimpleName() + "[", "]")
                .add("minimumOfRequiredMasterNodesForElection=" + minimumOfRequiredMasterNodesForElection)
                .toString();
    }
}
