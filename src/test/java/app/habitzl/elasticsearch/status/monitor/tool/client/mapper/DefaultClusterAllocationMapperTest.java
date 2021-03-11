package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.NodeAllocationDecision;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedReason;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils.JsonParser;
import app.habitzl.elasticsearch.status.monitor.util.TimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultClusterAllocationMapperTest {

    private static final String INDEX_NAME = "index-name";
    private static final int SHARD_NUMBER = 2;
    private static final boolean IS_PRIMARY_SHARD = false;
    private static final UnassignedReason UNASSIGNED_REASON = UnassignedReason.INDEX_CREATED;
    private static final String UNASSIGNED_SINCE_STRING = "2021-01-31T16:08:16.600Z";
    private static final Instant UNASSIGNED_SINCE = Instant.parse(UNASSIGNED_SINCE_STRING);
    private static final String ALLOCATE_EXPLANATION = "cannot allocate because allocation is not permitted to any of the nodes";
    private static final String NODE_ID_1 = "node-id-1";
    private static final String NODE_NAME_1 = "node-name-1";
    private static final String NODE_DECISION_EXPLANATION_1 = "node does not match index setting [some.setting.1]";
    private static final String NODE_ID_2 = "node-id-2";
    private static final String NODE_NAME_2 = "node-name-2";
    private static final String NODE_DECISION_EXPLANATION_2 = "node does not match index setting [some.setting.2]";

    private static final String JSON = "{\n"
            + "  \"index\": \"" + INDEX_NAME + "\",\n"
            + "  \"shard\": " + SHARD_NUMBER + ",\n"
            + "  \"primary\": " + IS_PRIMARY_SHARD + ",\n"
            + "  \"current_state\": \"" + "unassigned" + "\",\n"
            + "  \"unassigned_info\": {\n"
            + "    \"reason\": \"" + UNASSIGNED_REASON.name() + "\",\n"
            + "    \"at\": \"" + UNASSIGNED_SINCE.toString() + "\",\n"
            + "    \"last_allocation_status\": \"" + "no" + "\"\n"
            + "  },\n"
            + "  \"can_allocate\": \"" + "no" + "\",\n"
            + "  \"allocate_explanation\": \"" + ALLOCATE_EXPLANATION + "\",\n"
            + "  \"node_allocation_decisions\": [\n"
            + "    {\n"
            + "      \"node_id\": \"" + NODE_ID_1 + "\",\n"
            + "      \"node_name\": \"" + NODE_NAME_1 + "\",\n"
            + "      \"node_decision\": \"" + "no" + "\",\n"
            + "      \"deciders\": ["
            + "        {\n"
            + "          \"decider\": \"" + "filter" + "\",\n"
            + "          \"decision\": \"" + "NO" + "\",\n"
            + "          \"explanation\": \"" + NODE_DECISION_EXPLANATION_1 + "\"\n"
            + "        }\n"
            + "      ]\n"
            + "    },\n"
            + "    {\n"
            + "      \"node_id\": \"" + NODE_ID_2 + "\",\n"
            + "      \"node_name\": \"" + NODE_NAME_2 + "\",\n"
            + "      \"node_decision\": \"" + "no" + "\",\n"
            + "      \"deciders\": ["
            + "        {\n"
            + "          \"decider\": \"" + "filter" + "\",\n"
            + "          \"decision\": \"" + "NO" + "\",\n"
            + "          \"explanation\": \"" + NODE_DECISION_EXPLANATION_2 + "\"\n"
            + "        }\n"
            + "      ]\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    private DefaultClusterAllocationMapper sut;

    @BeforeEach
    void setUp() {
        JsonParser parser = new JsonParser(new ObjectMapper());
        TimeFormatter timeFormatter = prepareTimeFormatter();
        sut = new DefaultClusterAllocationMapper(parser, timeFormatter);
    }

    @Test
    void map_json_returnUnassignedShardInfo() {
        // Given
        List<NodeAllocationDecision> expectedNodeAllocationDecisions = List.of(
                new NodeAllocationDecision(NODE_ID_1, NODE_NAME_1, List.of(NODE_DECISION_EXPLANATION_1)),
                new NodeAllocationDecision(NODE_ID_2, NODE_NAME_2, List.of(NODE_DECISION_EXPLANATION_2))
        );
        UnassignedShardInfo expectedInfo = new UnassignedShardInfo(
                INDEX_NAME,
                SHARD_NUMBER,
                IS_PRIMARY_SHARD,
                UNASSIGNED_REASON,
                UNASSIGNED_SINCE,
                UNASSIGNED_SINCE_STRING,
                ALLOCATE_EXPLANATION,
                expectedNodeAllocationDecisions
        );

        // When
        UnassignedShardInfo result = sut.map(JSON);

        // Then
        assertThat(result, equalTo(expectedInfo));
    }

    @Test
    void map_invalidJson_returnEmptyInfo() {
        // Given
        UnassignedShardInfo expectedInfo = new UnassignedShardInfo(
                "",
                -1,
                false,
                UnassignedReason.UNKNOWN_REASON,
                null,
                "",
                "",
                List.of()
        );
        String invalidJson = "invalidJson";

        // When
        UnassignedShardInfo result = sut.map(invalidJson);

        // Then
        assertThat(result, equalTo(expectedInfo));
    }

    private TimeFormatter prepareTimeFormatter() {
        TimeFormatter timeFormatter = mock(TimeFormatter.class);
        when(timeFormatter.format((Instant) notNull())).thenReturn(UNASSIGNED_SINCE_STRING);
        when(timeFormatter.format((Instant) isNull())).thenReturn("");
        return timeFormatter;
    }
}