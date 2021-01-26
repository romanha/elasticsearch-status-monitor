package app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
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

    public <T> Optional<T> getValueFromPath(
            final String jsonData,
            final String path,
            final Class<T> valueType) {
        T result = null;
        try {
            JsonNode parent = mapper.readTree(jsonData);
            Optional<JsonNode> valueNode = getJsonValueNode(parent, path);

            if (valueNode.isPresent()) {
                result = mapper.treeToValue(valueNode.get(), valueType);
            }
        } catch (final JsonProcessingException | IllegalArgumentException e) {
            LOG.warn("Failed to parse JSON data.", e);
            result = null;
        }

        return Optional.ofNullable(result);
    }

    private Optional<JsonNode> getJsonValueNode(final JsonNode parentNode, final String path) {
        JsonNode valueNode = parentNode.at(path);
        if (!valueNode.isValueNode()) {
            valueNode = null;
        }

        return Optional.ofNullable(valueNode);
    }
}
