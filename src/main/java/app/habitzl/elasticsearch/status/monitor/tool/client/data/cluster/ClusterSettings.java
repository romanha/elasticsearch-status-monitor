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

    public static ClusterSettings createDefault() {
        return builder().build();
    }

    public static ClusterSettings.Builder builder() {
        return new ClusterSettings.Builder();
    }

    private ClusterSettings(final ClusterSettings.Builder builder) {
        this.minimumOfRequiredMasterNodesForElection = builder.minimumOfRequiredMasterNodesForElection;
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

    public static final class Builder {
        private int minimumOfRequiredMasterNodesForElection = ClusterSettings.ES_DEFAULT_MINIMUM_REQUIRED_MASTER_NODES_FOR_ELECTION;

        private Builder() {
        }

        public ClusterSettings.Builder withMinimumOfRequiredMasterNodesForElection(int minimumOfRequiredMasterNodesForElection) {
            this.minimumOfRequiredMasterNodesForElection = minimumOfRequiredMasterNodesForElection;
            return this;
        }

        public ClusterSettings build() {
            return new ClusterSettings(this);
        }
    }
}
