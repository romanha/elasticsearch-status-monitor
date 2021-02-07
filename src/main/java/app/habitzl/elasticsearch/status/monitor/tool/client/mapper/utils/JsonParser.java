package app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A mapper class for JSON operations.
 */
public class JsonParser {
    private static final Logger LOG = LogManager.getLogger(JsonParser.class);

    private final ObjectMapper mapper;

    @Inject
    public JsonParser(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Gets a value of the specified type from the given path in the provided JSON data.
     *
     * @return The value or empty if the path does not exist or the specified type does not match.
     */
    public <T> Optional<T> getValueFromPath(
            final String jsonData,
            final String path,
            final Class<T> valueType) {
        Optional<T> result;
        try {
            JsonNode parent = mapper.readTree(jsonData);
            result = getValueFromJsonObject(parent, path, valueType);
        } catch (final JsonProcessingException e) {
            LOG.warn("Failed to parse JSON data.", e);
            result = Optional.empty();
        }

        return result;
    }

    /**
     * Gets a value of the specified type from the given path in the provided JSON object.
     *
     * @return The value or empty if the path does not exist or the specified type does not match.
     */
    public <T> Optional<T> getValueFromJsonObject(
            final JsonNode objectNode,
            final String path,
            final Class<T> valueType) {
        T result = null;
        try {
            if (objectNode.isObject()) {
                Optional<JsonNode> valueNode = getJsonValueNode(objectNode, path);
                if (valueNode.isPresent()) {
                    result = mapper.treeToValue(valueNode.get(), valueType);
                }
            }
        } catch (final JsonProcessingException | IllegalArgumentException e) {
            LOG.warn("Failed to parse JSON data.", e);
            result = null;
        }

        return Optional.ofNullable(result);
    }

    /**
     * Gets a list of child elements from the given path in the provided JSON data.
     *
     * @return The list of JSON nodes.
     */
    public List<JsonNode> getListFromPath(final String jsonData, final String path) {
        List<JsonNode> result = new ArrayList<>();
        try {
            JsonNode parent = mapper.readTree(jsonData);
            Optional<ArrayNode> arrayNode = getJsonArrayNode(parent, path);
            arrayNode.ifPresent(jsonNodes -> jsonNodes.elements().forEachRemaining(result::add));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    private Optional<JsonNode> getJsonValueNode(final JsonNode parentNode, final String path) {
        JsonNode valueNode = parentNode.at(path);
        if (!valueNode.isValueNode()) {
            valueNode = null;
        }

        return Optional.ofNullable(valueNode);
    }

    private Optional<ArrayNode> getJsonArrayNode(final JsonNode parentNode, final String path) {
        ArrayNode arrayNode;
        JsonNode nodeAtPath = parentNode.at(path);
        if (nodeAtPath.isArray()) {
            arrayNode = (ArrayNode) nodeAtPath;
        } else {
            arrayNode = null;
        }

        return Optional.ofNullable(arrayNode);
    }
}
