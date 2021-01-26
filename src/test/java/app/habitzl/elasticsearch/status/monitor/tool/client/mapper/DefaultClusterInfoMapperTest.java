package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterHealthStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DefaultClusterInfoMapperTest {

    private static final String CLUSTER_NAME = "cluster-name";
    private static final ClusterHealthStatus HEALTH_STATUS = ClusterHealthStatus.YELLOW;
    private static final int NUMBER_OF_NODES = 10;
    private static final int NUMBER_OF_DATA_NODES = 5;
    private static final int ACTIVE_PRIMARY_SHARDS = 13;
    private static final int ACTIVE_SHARDS = 42;
    private static final int INITIALIZING_SHARDS = 7;
    private static final int UNASSIGNED_SHARDS = 17;

    private static final String JSON = "{\n"
            + "  \"cluster_name\": \"" + CLUSTER_NAME + "\",\n"
            + "  \"status\": \"" + HEALTH_STATUS.name().toLowerCase() + "\",\n"
            + "  \"number_of_nodes\": " + NUMBER_OF_NODES + ",\n"
            + "  \"number_of_data_nodes\": " + NUMBER_OF_DATA_NODES + ",\n"
            + "  \"active_primary_shards\": " + ACTIVE_PRIMARY_SHARDS + ",\n"
            + "  \"active_shards\": " + ACTIVE_SHARDS + ",\n"
            + "  \"initializing_shards\": " + INITIALIZING_SHARDS + ",\n"
            + "  \"unassigned_shards\": " + UNASSIGNED_SHARDS + "\n"
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
        ClusterInfo expectedInfo = new ClusterInfo(
                CLUSTER_NAME,
                HEALTH_STATUS,
                NUMBER_OF_NODES,
                NUMBER_OF_DATA_NODES,
                ACTIVE_SHARDS,
                ACTIVE_PRIMARY_SHARDS,
                INITIALIZING_SHARDS,
                UNASSIGNED_SHARDS
        );

        // When
        ClusterInfo result = sut.map(JSON);

        // Then
        assertThat(result, equalTo(expectedInfo));
    }

    @Test
    void map_invalidJson_returnEmptyClusterInfo() {
        // Given
        ClusterInfo expectedInfo = new ClusterInfo(
                "",
                ClusterHealthStatus.UNKNOWN,
                0,
                0,
                0,
                0,
                0,
                0
        );

        // When
        ClusterInfo result = sut.map("");

        // Then
        assertThat(result, equalTo(expectedInfo));
    }
}