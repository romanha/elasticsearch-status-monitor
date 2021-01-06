package app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public final class ClusterSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int minimumOfRequiredMasterNodesForElection;

    public static ClusterSettings createDefault() {
        return new ClusterSettings(0);
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
