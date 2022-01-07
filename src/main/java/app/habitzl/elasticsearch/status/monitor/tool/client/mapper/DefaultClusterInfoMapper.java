package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterHealthStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterStats;
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

    private static final String MASTER_NODE_ID_PATH = "/master_node";

    private static final String NUMBER_OF_INDICES_PATH = "/indices/count";
    private static final String NUMBER_OF_SHARDS_PATH = "/indices/shards/total";
    private static final String NUMBER_OF_PRIMARY_SHARDS_PATH = "/indices/shards/primaries";
    private static final String NUMBER_OF_DOCUMENTS_PATH = "/indices/docs/count";
    private static final String NUMBER_OF_NODES_PATH_2 = "/nodes/count/total";
    private static final String NUMBER_OF_DATA_NODES_PATH_2 = "/nodes/count/data";
    private static final String NUMBER_OF_MASTER_ELIGIBLE_NODES_PATH = "/nodes/count/master";

    private final JsonParser parser;

    @Inject
    public DefaultClusterInfoMapper(final JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public ClusterInfo map(final String clusterHealthJson, final String clusterStateJson, final String clusterStatsJson) {
        Optional<String> clusterName = parser.getValueFromPath(clusterHealthJson, CLUSTER_NAME_PATH, String.class);
        ClusterHealthStatus healthStatus = parser.getValueFromPath(clusterHealthJson, HEALTH_STATUS_PATH, String.class)
                .map(String::toUpperCase)
                .map(ClusterHealthStatus::valueOf)
                .orElse(ClusterHealthStatus.UNKNOWN);
        Optional<Integer> numberOfNodes = parser.getValueFromPath(clusterHealthJson, NUMBER_OF_NODES_PATH, Integer.class);
        Optional<Integer> numberOfDataNodes = parser.getValueFromPath(clusterHealthJson, NUMBER_OF_DATA_NODES_PATH, Integer.class);
        Optional<Integer> activeShards = parser.getValueFromPath(clusterHealthJson, ACTIVE_SHARDS_PATH, Integer.class);
        Optional<Integer> activePrimaryShards = parser.getValueFromPath(clusterHealthJson, ACTIVE_PRIMARY_SHARDS_PATH, Integer.class);
        Optional<Integer> initializingShards = parser.getValueFromPath(clusterHealthJson, INITIALIZING_SHARDS_PATH, Integer.class);
        Optional<Integer> unassignedShards = parser.getValueFromPath(clusterHealthJson, UNASSIGNED_SHARDS_PATH, Integer.class);

        Optional<String> masterNodeId = parser.getValueFromPath(clusterStateJson, MASTER_NODE_ID_PATH, String.class);

        ClusterStats clusterStats = gatherClusterStats(clusterStatsJson);

        return new ClusterInfo(
                clusterName.orElse(""),
                healthStatus,
                numberOfNodes.orElse(0),
                numberOfDataNodes.orElse(0),
                activeShards.orElse(0),
                activePrimaryShards.orElse(0),
                initializingShards.orElse(0),
                unassignedShards.orElse(0),
                masterNodeId.orElse(""),
                clusterStats
        );
    }

    private ClusterStats gatherClusterStats(final String clusterStatsJson) {
        Optional<Integer> numberOfIndices = parser.getValueFromPath(clusterStatsJson, NUMBER_OF_INDICES_PATH, Integer.class);
        Optional<Integer> numberOfDocuments = parser.getValueFromPath(clusterStatsJson, NUMBER_OF_DOCUMENTS_PATH, Integer.class);
        Optional<Integer> numberOfNodes = parser.getValueFromPath(clusterStatsJson, NUMBER_OF_NODES_PATH_2, Integer.class);
        Optional<Integer> numberOfMasterEligibleNodes = parser.getValueFromPath(clusterStatsJson, NUMBER_OF_MASTER_ELIGIBLE_NODES_PATH, Integer.class);
        Optional<Integer> numberOfDataNodes = parser.getValueFromPath(clusterStatsJson, NUMBER_OF_DATA_NODES_PATH_2, Integer.class);
        Optional<Integer> numberOfShards = parser.getValueFromPath(clusterStatsJson, NUMBER_OF_SHARDS_PATH, Integer.class);
        Optional<Integer> numberOfPrimaryShards = parser.getValueFromPath(clusterStatsJson, NUMBER_OF_PRIMARY_SHARDS_PATH, Integer.class);

        return new ClusterStats(
                numberOfIndices.orElse(0),
                numberOfDocuments.orElse(0),
                numberOfNodes.orElse(0),
                numberOfMasterEligibleNodes.orElse(0),
                numberOfDataNodes.orElse(0),
                numberOfShards.orElse(0),
                numberOfPrimaryShards.orElse(0)
        );
    }
}
