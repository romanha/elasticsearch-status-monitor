package app.habitzl.elasticsearch.status.monitor.data;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public class ClusterHealth {

	private final String clusterName;
	private final HealthStatus healthStatus;
	private final int numberOfNodes;
	private final int numberOfDataNodes;
	private final int numberOfActiveShards;
	private final int numberOfInitializingShards;
	private final int numberOfUnassignedShards;

	public static ClusterHealth unknown() {
		return new ClusterHealth("unknown", HealthStatus.UNKNOWN, -1, -1, -1, -1, -1);
	}

	public static ClusterHealth fromClusterHealthResponse(final ClusterHealthResponse response) {
		return new ClusterHealth(
				response.getClusterName(),
				HealthStatus.valueOf(response.getStatus().name()),
				response.getNumberOfNodes(),
				response.getNumberOfDataNodes(),
				response.getActiveShards(),
				response.getInitializingShards(),
				response.getUnassignedShards()
		);
	}

	private ClusterHealth(
			final String clusterName,
			final HealthStatus healthStatus,
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

	public HealthStatus getHealthStatus() {
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
	public String toString() {
		final StringBuilder sb = new StringBuilder("ClusterHealth{");
		sb.append("clusterName='").append(clusterName).append('\'');
		sb.append(", healthStatus=").append(healthStatus);
		sb.append(", numberOfNodes=").append(numberOfNodes);
		sb.append(", numberOfDataNodes=").append(numberOfDataNodes);
		sb.append(", numberOfActiveShards=").append(numberOfActiveShards);
		sb.append(", numberOfInitializingShards=").append(numberOfInitializingShards);
		sb.append(", numberOfUnassignedShards=").append(numberOfUnassignedShards);
		sb.append('}');
		return sb.toString();
	}
}
