package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterHealthStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterStats;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DefaultClusterInfoMapperTest {

    private static final String CLUSTER_NAME = "cluster-name";
    private static final String CLUSTER_UUID = "cluster-uuid";
    private static final ClusterHealthStatus HEALTH_STATUS = ClusterHealthStatus.YELLOW;
    private static final int NUMBER_OF_NODES = 10;
    private static final int NUMBER_OF_DATA_NODES = 5;
    private static final int NUMBER_OF_MASTER_ELIGIBLE_NODES = 3;
    private static final int ACTIVE_PRIMARY_SHARDS = 13;
    private static final int ACTIVE_SHARDS = 42;
    private static final int INITIALIZING_SHARDS = 7;
    private static final int UNASSIGNED_SHARDS = 17;
    private static final int NUMBER_OF_INDICES = 78;
    private static final int NUMBER_OF_SHARDS = 66;
    private static final int NUMBER_OF_PRIMARY_SHARDS = 16;
    private static final int NUMBER_OF_DOCUMENTS = 923107;

    private static final String MASTER_NODE_ID = "master-node-id";

    private static final String JSON_CLUSTER_HEALTH = "{\n"
            + "  \"cluster_name\": \"" + CLUSTER_NAME + "\",\n"
            + "  \"status\": \"" + HEALTH_STATUS.name().toLowerCase() + "\",\n"
            + "  \"number_of_nodes\": " + NUMBER_OF_NODES + ",\n"
            + "  \"number_of_data_nodes\": " + NUMBER_OF_DATA_NODES + ",\n"
            + "  \"active_primary_shards\": " + ACTIVE_PRIMARY_SHARDS + ",\n"
            + "  \"active_shards\": " + ACTIVE_SHARDS + ",\n"
            + "  \"initializing_shards\": " + INITIALIZING_SHARDS + ",\n"
            + "  \"unassigned_shards\": " + UNASSIGNED_SHARDS + "\n"
            + "}";
    private static final String JSON_CLUSTER_STATE = "{\n"
            + "  \"cluster_name\": \"" + CLUSTER_NAME + "\",\n"
            + "  \"cluster_uuid\": \"" + CLUSTER_UUID + "\",\n"
            + "  \"master_node\": \"" + MASTER_NODE_ID + "\"\n"
            + "}";
    private static final String JSON_CLUSTER_STATS = "{\n"
            + "  \"cluster_name\": \"" + CLUSTER_NAME + "\",\n"
            + "  \"status\": \"" + HEALTH_STATUS.name().toLowerCase() + "\",\n"
            + "  \"indices\": {\n"
            + "    \"count\": " + NUMBER_OF_INDICES + ",\n"
            + "    \"shards\": {\n"
            + "      \"total\": " + NUMBER_OF_SHARDS + ",\n"
            + "      \"primaries\": " + NUMBER_OF_PRIMARY_SHARDS + ",\n"
            + "      \"replication\": 3.125\n" // this is the overall amount of replication in percent (replicas / primaries)
            + "    },\n"
            + "    \"docs\": {\n"
            + "      \"count\": " + NUMBER_OF_DOCUMENTS + ",\n"
            + "      \"deleted\": 0\n"
            + "    }\n"
            + "  },\n"
            + "  \"nodes\": {\n"
            + "    \"count\": {\n"
            + "      \"total\": " + NUMBER_OF_NODES + ",\n"
            + "      \"data\": " + NUMBER_OF_DATA_NODES + ",\n"
            + "      \"master\": " + NUMBER_OF_MASTER_ELIGIBLE_NODES + "\n"
            + "    }\n"
            + "  }\n"
            + "}";

    private DefaultClusterInfoMapper sut;

    @BeforeEach
    void setUp() {
        JsonParser parser = new JsonParser(new ObjectMapper());
        sut = new DefaultClusterInfoMapper(parser);
    }

    @Test
    void map_json_returnClusterInfo() {
        // Given
        ClusterStats expectedStats = new ClusterStats(
                NUMBER_OF_INDICES,
                NUMBER_OF_DOCUMENTS,
                NUMBER_OF_NODES,
                NUMBER_OF_MASTER_ELIGIBLE_NODES,
                NUMBER_OF_DATA_NODES,
                NUMBER_OF_SHARDS,
                NUMBER_OF_PRIMARY_SHARDS
        );
        ClusterInfo expectedInfo = new ClusterInfo(
                CLUSTER_NAME,
                HEALTH_STATUS,
                NUMBER_OF_NODES,
                NUMBER_OF_DATA_NODES,
                ACTIVE_SHARDS,
                ACTIVE_PRIMARY_SHARDS,
                INITIALIZING_SHARDS,
                UNASSIGNED_SHARDS,
                MASTER_NODE_ID,
                expectedStats
        );

        // When
        ClusterInfo result = sut.map(JSON_CLUSTER_HEALTH, JSON_CLUSTER_STATE, JSON_CLUSTER_STATS);

        // Then
        assertThat(result, equalTo(expectedInfo));
    }

    @Test
    void map_invalidJson_returnEmptyClusterInfo() {
        // Given
        ClusterStats expectedStats = new ClusterStats(
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );
        ClusterInfo expectedInfo = new ClusterInfo(
                "",
                ClusterHealthStatus.UNKNOWN,
                0,
                0,
                0,
                0,
                0,
                0,
                "",
                expectedStats
        );

        // When
        ClusterInfo result = sut.map("", "", "");

        // Then
        assertThat(result, equalTo(expectedInfo));
    }
}