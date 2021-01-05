package app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public final class ClusterInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String clusterName;
    private final ClusterHealthStatus healthStatus;
    private final int numberOfNodes;
    private final int numberOfDataNodes;
    private final int numberOfActiveShards;
    private final int numberOfInitializingShards;
    private final int numberOfUnassignedShards;

    public static ClusterInfo fromClusterHealthResponse(final ClusterHealthResponse response) {
        return new ClusterInfo(
                response.getClusterName(),
                ClusterHealthStatus.valueOf(response.getStatus().name()),
                response.getNumberOfNodes(),
                response.getNumberOfDataNodes(),
                response.getActiveShards(),
                response.getInitializingShards(),
                response.getUnassignedShards()
        );
    }

    public ClusterInfo(
            final String clusterName,
            final ClusterHealthStatus healthStatus,
            final int numberOfNodes,
            final int numberOfDataNodes,
            final int numberOfActiveShards,
            final int numberOfInitializingShards,
            final int numberOfUnassignedShards) {
        this.clusterName = clusterName;
        this.healthStatus = healthStatus;
        this.numberOfNodes = numberOfNodes;
        this.numberOfDataNodes = numberOfDataNodes;
        this.numberOfActiveShards = numberOfActiveShards;
        this.numberOfInitializingShards = numberOfInitializingShards;
        this.numberOfUnassignedShards = numberOfUnassignedShards;
    }

    public String getClusterName() {
        return clusterName;
    }

    public ClusterHealthStatus getHealthStatus() {
        return healthStatus;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getNumberOfDataNodes() {
        return numberOfDataNodes;
    }

    public int getNumberOfActiveShards() {
        return numberOfActiveShards;
    }

    public int getNumberOfInitializingShards() {
        return numberOfInitializingShards;
    }

    public int getNumberOfUnassignedShards() {
        return numberOfUnassignedShards;
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
            ClusterInfo that = (ClusterInfo) o;
            isEqual = Objects.equals(numberOfNodes, that.numberOfNodes)
                    && Objects.equals(numberOfDataNodes, that.numberOfDataNodes)
                    && Objects.equals(numberOfActiveShards, that.numberOfActiveShards)
                    && Objects.equals(numberOfInitializingShards, that.numberOfInitializingShards)
                    && Objects.equals(numberOfUnassignedShards, that.numberOfUnassignedShards)
                    && Objects.equals(clusterName, that.clusterName)
                    && Objects.equals(healthStatus, that.healthStatus);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                clusterName, healthStatus, numberOfNodes, numberOfDataNodes, numberOfActiveShards, numberOfInitializingShards, numberOfUnassignedShards);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ClusterInfo.class.getSimpleName() + "[", "]")
                .add("clusterName='" + clusterName + "'")
                .add("healthStatus=" + healthStatus)
                .add("numberOfNodes=" + numberOfNodes)
                .add("numberOfDataNodes=" + numberOfDataNodes)
                .add("numberOfActiveShards=" + numberOfActiveShards)
                .add("numberOfInitializingShards=" + numberOfInitializingShards)
                .add("numberOfUnassignedShards=" + numberOfUnassignedShards)
                .toString();
    }
}
