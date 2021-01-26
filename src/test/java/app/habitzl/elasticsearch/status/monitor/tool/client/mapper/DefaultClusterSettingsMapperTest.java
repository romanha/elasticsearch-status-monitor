package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultClusterSettingsMapperTest {
    private static final int MINIMUM_MASTER_NODES = 2;

    private static final String JSON_SETTINGS_TRANSIENT = "{\n"
            + "  \"transient\": {\n"
            + "    \"discovery\": {\n"
            + "      \"type\": \"zen\",\n"
            + "      \"zen\": {\n"
            + "        \"minimum_master_nodes\": \"" + MINIMUM_MASTER_NODES + "\"\n"
            + "      }\n"
            + "    }\n"
            + "  },\n"
            + "  \"persistent\": {},\n"
            + "  \"defaults\": {}\n"
            + "}";

    private static final String JSON_SETTINGS_PERSISTENT = "{\n"
            + "  \"transient\": {},\n"
            + "  \"persistent\": {\n"
            + "    \"discovery\": {\n"
            + "      \"type\": \"zen\",\n"
            + "      \"zen\": {\n"
            + "        \"minimum_master_nodes\": \"" + MINIMUM_MASTER_NODES + "\"\n"
            + "      }\n"
            + "    }\n"
            + "  },\n"
            + "  \"defaults\": {}\n"
            + "}";

    private static final String JSON_SETTINGS_DEFAULTS = "{\n"
            + "  \"transient\": {},\n"
            + "  \"persistent\": {},\n"
            + "  \"defaults\": {\n"
            + "    \"discovery\": {\n"
            + "      \"type\": \"zen\",\n"
            + "      \"zen\": {\n"
            + "        \"minimum_master_nodes\": \"" + MINIMUM_MASTER_NODES + "\"\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "}";

    private DefaultClusterSettingsMapper sut;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = mock(ObjectMapper.class);
        sut = new DefaultClusterSettingsMapper(mapper);
    }

    @ParameterizedTest
    @ValueSource(strings = {JSON_SETTINGS_TRANSIENT, JSON_SETTINGS_PERSISTENT, JSON_SETTINGS_DEFAULTS})
    void map_jsonData_returnsExpectedSettings(final String jsonData) {
        // Given
        sut = new DefaultClusterSettingsMapper(new ObjectMapper());

        // When
        ClusterSettings result = sut.map(jsonData);

        // Then
        ClusterSettings expected = ClusterSettings.builder()
                                                  .withMinimumOfRequiredMasterNodesForElection(MINIMUM_MASTER_NODES)
                                                  .build();
        assertThat(result, equalTo(expected));
    }

    @Test
    void map_mapperThrowsException_returnDefaultSettings() throws Exception {
        // Given
        String jsonData = "json";
        when(mapper.readTree(jsonData)).thenThrow(JsonProcessingException.class);

        // When
        ClusterSettings result = sut.map(jsonData);

        // Then
        ClusterSettings expected = ClusterSettings.createDefault();
        assertThat(result, equalTo(expected));
    }
}