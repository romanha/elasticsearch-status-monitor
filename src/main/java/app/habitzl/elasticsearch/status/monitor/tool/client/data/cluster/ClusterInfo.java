package app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster;

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
    private final int numberOfPrimaryShards;
    private final int numberOfInitializingShards;
    private final int numberOfUnassignedShards;
    private final String masterNodeId;

    public ClusterInfo(
            final String clusterName,
            final ClusterHealthStatus healthStatus,
            final int numberOfNodes,
            final int numberOfDataNodes,
            final int numberOfActiveShards,
            final int numberOfPrimaryShards,
            final int numberOfInitializingShards,
            final int numberOfUnassignedShards,
            final String masterNodeId) {
        this.clusterName = clusterName;
        this.healthStatus = healthStatus;
        this.numberOfNodes = numberOfNodes;
        this.numberOfDataNodes = numberOfDataNodes;
        this.numberOfActiveShards = numberOfActiveShards;
        this.numberOfPrimaryShards = numberOfPrimaryShards;
        this.numberOfInitializingShards = numberOfInitializingShards;
        this.numberOfUnassignedShards = numberOfUnassignedShards;
        this.masterNodeId = masterNodeId;
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

    public int getNumberOfPrimaryShards() {
        return numberOfPrimaryShards;
    }

    public int getNumberOfInitializingShards() {
        return numberOfInitializingShards;
    }

    public int getNumberOfUnassignedShards() {
        return numberOfUnassignedShards;
    }

    public String getMasterNodeId() {
        return masterNodeId;
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
                    && Objects.equals(numberOfPrimaryShards, that.numberOfPrimaryShards)
                    && Objects.equals(numberOfInitializingShards, that.numberOfInitializingShards)
                    && Objects.equals(numberOfUnassignedShards, that.numberOfUnassignedShards)
                    && Objects.equals(clusterName, that.clusterName)
                    && Objects.equals(healthStatus, that.healthStatus)
                    && Objects.equals(masterNodeId, that.masterNodeId);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clusterName, healthStatus, numberOfNodes, numberOfDataNodes, numberOfActiveShards, numberOfPrimaryShards, numberOfInitializingShards, numberOfUnassignedShards, masterNodeId);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ClusterInfo.class.getSimpleName() + "[", "]")
                .add("clusterName='" + clusterName + "'")
                .add("healthStatus=" + healthStatus)
                .add("numberOfNodes=" + numberOfNodes)
                .add("numberOfDataNodes=" + numberOfDataNodes)
                .add("numberOfActiveShards=" + numberOfActiveShards)
                .add("numberOfPrimaryShards=" + numberOfPrimaryShards)
                .add("numberOfInitializingShards=" + numberOfInitializingShards)
                .add("numberOfUnassignedShards=" + numberOfUnassignedShards)
                .add("masterNodeId='" + masterNodeId + "'")
                .toString();
    }
}
