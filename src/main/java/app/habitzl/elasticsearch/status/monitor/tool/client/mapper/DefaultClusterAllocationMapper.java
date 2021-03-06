package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.NodeAllocationDecision;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedReason;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultClusterAllocationMapper implements ClusterAllocationMapper {
    private static final Logger LOG = LogManager.getLogger(DefaultClusterAllocationMapper.class);

    private static final String INDEX_NAME_PATH = "/index";
    private static final String SHARD_NUMBER_PATH = "/shard";
    private static final String IS_PRIMARY_PATH = "/primary";
    private static final String UNASSIGNED_INFO_REASON_PATH = "/unassigned_info/reason";
    private static final String UNASSIGNED_INFO_TIMESTAMP_PATH = "/unassigned_info/at";
    private static final String ALLOCATION_EXPLANATION_PATH = "/allocate_explanation";
    private static final String ALLOCATION_DECISIONS_PATH = "/node_allocation_decisions";
    private static final String ALLOCATION_DECISION_NODE_ID_PATH = "/node_id";
    private static final String ALLOCATION_DECISION_NODE_NAME_PATH = "/node_name";
    private static final String ALLOCATION_DECISION_DECIDERS_PATH = "/deciders";
    private static final String ALLOCATION_DECISION_DECIDER_EXPLANATION_PATH = "/explanation";

    private final JsonParser parser;
    private final TimeFormatter timeFormatter;

    @Inject
    public DefaultClusterAllocationMapper(final JsonParser parser, final TimeFormatter timeFormatter) {
        this.parser = parser;
        this.timeFormatter = timeFormatter;
    }

    @Override
    public UnassignedShardInfo map(final String jsonData) {
        Optional<String> indexName = parser.getValueFromPath(jsonData, INDEX_NAME_PATH, String.class);
        Optional<Integer> shardNumber = parser.getValueFromPath(jsonData, SHARD_NUMBER_PATH, Integer.class);
        Optional<Boolean> isPrimaryShard = parser.getValueFromPath(jsonData, IS_PRIMARY_PATH, Boolean.class);
        UnassignedReason unassignedReason = parser.getValueFromPath(jsonData, UNASSIGNED_INFO_REASON_PATH, String.class)
                                                  .map(String::toUpperCase)
                                                  .map(UnassignedReason::valueOf)
                                                  .orElse(UnassignedReason.UNKNOWN_REASON);
        Instant unassignedSince = parser.getValueFromPath(jsonData, UNASSIGNED_INFO_TIMESTAMP_PATH, String.class)
                                        .flatMap(this::parseTimestamp)
                                        .orElse(null);
        Optional<String> explanation = parser.getValueFromPath(jsonData, ALLOCATION_EXPLANATION_PATH, String.class);

        List<NodeAllocationDecision> decisions = mapNodeAllocationDecisions(jsonData);

        return new UnassignedShardInfo(
                indexName.orElse(""),
                shardNumber.orElse(-1),
                isPrimaryShard.orElse(false),
                unassignedReason,
                unassignedSince,
                timeFormatter.format(unassignedSince),
                explanation.orElse(""),
                decisions
        );
    }

    private List<NodeAllocationDecision> mapNodeAllocationDecisions(final String jsonData) {
        List<NodeAllocationDecision> decisions = new ArrayList<>();
        List<JsonNode> decisionNodes = parser.getListFromPath(jsonData, ALLOCATION_DECISIONS_PATH);
        decisionNodes.forEach(node -> {
            Optional<String> nodeId = parser.getValueFromJsonObject(node, ALLOCATION_DECISION_NODE_ID_PATH, String.class);
            Optional<String> nodeName = parser.getValueFromJsonObject(node, ALLOCATION_DECISION_NODE_NAME_PATH, String.class);

            List<String> explanations = mapNodeAllocationExplanations(node);
            NodeAllocationDecision decision = new NodeAllocationDecision(
                    nodeId.orElse(""),
                    nodeName.orElse(""),
                    explanations
            );
            decisions.add(decision);
        });

        return decisions;
    }

    private List<String> mapNodeAllocationExplanations(final JsonNode allocationDecisionsNode) {
        List<JsonNode> deciderNodes = parser.getListFromPath(allocationDecisionsNode.toPrettyString(), ALLOCATION_DECISION_DECIDERS_PATH);
        return deciderNodes.stream()
                           .map(n -> parser.getValueFromJsonObject(n, ALLOCATION_DECISION_DECIDER_EXPLANATION_PATH, String.class))
                           .filter(Optional::isPresent)
                           .map(Optional::get)
                           .collect(Collectors.toList());
    }

    private Optional<Instant> parseTimestamp(final String timestamp) {
        Instant result = null;
        try {
            result = Instant.parse(timestamp);
        } catch (final DateTimeParseException e) {
            LOG.warn("Failed to parse timestamp '{}'.", timestamp);
        }

        return Optional.ofNullable(result);
    }
}
