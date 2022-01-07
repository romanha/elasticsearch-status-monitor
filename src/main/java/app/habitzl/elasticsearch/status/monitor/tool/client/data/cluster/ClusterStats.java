package app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public final class ClusterStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int numberOfIndices;
    private final int numberOfDocuments;
    private final int numberOfNodes;
    private final int numberOfMasterEligibleNodes;
    private final int numberOfDataNodes;
    private final int numberOfShards;
    private final int numberOfPrimaryShards;

    public ClusterStats(
            final int numberOfIndices,
            final int numberOfDocuments,
            final int numberOfNodes,
            final int numberOfMasterEligibleNodes,
            final int numberOfDataNodes,
            final int numberOfShards,
            final int numberOfPrimaryShards) {
        this.numberOfIndices = numberOfIndices;
        this.numberOfDocuments = numberOfDocuments;
        this.numberOfNodes = numberOfNodes;
        this.numberOfMasterEligibleNodes = numberOfMasterEligibleNodes;
        this.numberOfDataNodes = numberOfDataNodes;
        this.numberOfShards = numberOfShards;
        this.numberOfPrimaryShards = numberOfPrimaryShards;
    }

    public int getNumberOfIndices() {
        return numberOfIndices;
    }

    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getNumberOfMasterEligibleNodes() {
        return numberOfMasterEligibleNodes;
    }

    public int getNumberOfDataNodes() {
        return numberOfDataNodes;
    }

    public int getNumberOfShards() {
        return numberOfShards;
    }

    public int getNumberOfPrimaryShards() {
        return numberOfPrimaryShards;
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
            ClusterStats that = (ClusterStats) o;
            isEqual = Objects.equals(numberOfIndices, that.numberOfIndices)
                    && Objects.equals(numberOfDocuments, that.numberOfDocuments)
                    && Objects.equals(numberOfNodes, that.numberOfNodes)
                    && Objects.equals(numberOfMasterEligibleNodes, that.numberOfMasterEligibleNodes)
                    && Objects.equals(numberOfDataNodes, that.numberOfDataNodes)
                    && Objects.equals(numberOfShards, that.numberOfShards)
                    && Objects.equals(numberOfPrimaryShards, that.numberOfPrimaryShards);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                numberOfIndices,
                numberOfDocuments,
                numberOfNodes,
                numberOfMasterEligibleNodes,
                numberOfDataNodes,
                numberOfShards,
                numberOfPrimaryShards
        );
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ClusterStats.class.getSimpleName() + "[", "]")
                .add("numberOfIndices=" + numberOfIndices)
                .add("numberOfDocuments=" + numberOfDocuments)
                .add("numberOfNodes=" + numberOfNodes)
                .add("numberOfMasterEligibleNodes=" + numberOfMasterEligibleNodes)
                .add("numberOfDataNodes=" + numberOfDataNodes)
                .add("numberOfShards=" + numberOfShards)
                .add("numberOfPrimaryShards=" + numberOfPrimaryShards)
                .toString();
    }
}
