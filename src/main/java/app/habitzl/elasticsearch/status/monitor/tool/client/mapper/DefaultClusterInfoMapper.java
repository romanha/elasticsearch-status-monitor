package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterHealthStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils.JsonParser;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultClusterInfoMapper implements ClusterInfoMapper {

    private static final String CLUSTER_NAME_PATH = "/cluster_name";
    private static final String HEALTH_STATUS_PATH = "/status";
    private static final String NUMBER_OF_NODES_PATH = "/number_of_nodes";
    private static final String NUMBER_OF_DATA_NODES_PATH = "/number_of_data_nodes";
    private static final String ACTIVE_SHARDS_PATH = "/active_shards";
    private static final String ACTIVE_PRIMARY_SHARDS_PATH = "/active_primary_shards";
    private static final String INITIALIZING_SHARDS_PATH = "/initializing_shards";
    private static final String UNASSIGNED_SHARDS_PATH = "/unassigned_shards";

    private final JsonParser parser;

    @Inject
    public DefaultClusterInfoMapper(final JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public ClusterInfo map(final String jsonData) {
        Optional<String> clusterName = parser.getValueFromPath(jsonData, CLUSTER_NAME_PATH, String.class);
        ClusterHealthStatus healthStatus = parser.getValueFromPath(jsonData, HEALTH_STATUS_PATH, String.class)
                                                 .map(String::toUpperCase)
                                                 .map(ClusterHealthStatus::valueOf)
                                                 .orElse(ClusterHealthStatus.UNKNOWN);
        Optional<Integer> numberOfNodes = parser.getValueFromPath(jsonData, NUMBER_OF_NODES_PATH, Integer.class);
        Optional<Integer> numberOfDataNodes = parser.getValueFromPath(jsonData, NUMBER_OF_DATA_NODES_PATH, Integer.class);
        Optional<Integer> activeShards = parser.getValueFromPath(jsonData, ACTIVE_SHARDS_PATH, Integer.class);
        Optional<Integer> activePrimaryShards = parser.getValueFromPath(jsonData, ACTIVE_PRIMARY_SHARDS_PATH, Integer.class);
        Optional<Integer> initializingShards = parser.getValueFromPath(jsonData, INITIALIZING_SHARDS_PATH, Integer.class);
        Optional<Integer> unassignedShards = parser.getValueFromPath(jsonData, UNASSIGNED_SHARDS_PATH, Integer.class);

        return new ClusterInfo(
                clusterName.orElse(""),
                healthStatus,
                numberOfNodes.orElse(0),
                numberOfDataNodes.orElse(0),
                activeShards.orElse(0),
                activePrimaryShards.orElse(0),
                initializingShards.orElse(0),
                unassignedShards.orElse(0)
        );
    }
}
