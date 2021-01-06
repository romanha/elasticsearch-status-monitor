package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultClusterSettingsMapper implements ClusterSettingsMapper {
    private static final Logger LOG = LogManager.getLogger(DefaultClusterSettingsMapper.class);

    private static final String TRANSIENT_SETTINGS_PATH_PREFIX = "/transient/";
    private static final String PERSISTENT_SETTINGS_PATH_PREFIX = "/persistent/";
    private static final String DEFAULT_SETTINGS_PATH_PREFIX = "/defaults/";
    private static final String PATH_MINIMUM_MASTER_NODES = "cluster/discovery/minimum_master_nodes";

    private final ObjectMapper mapper;

    @Inject
    public DefaultClusterSettingsMapper(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ClusterSettings map(final String jsonData) {
        int minimumMasterNodes = getValueFromAnySettingsType(jsonData, PATH_MINIMUM_MASTER_NODES, Integer.class)
                .orElse(0);
        return new ClusterSettings(minimumMasterNodes);
    }

    /**
     * Attempts to get the value of the given type from the given path.
     * The settings are searched in order of their precedence: transient, persistent, defaults.
     *
     * @see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/cluster-update-settings.html">ES cluster settings precedence</a>
     */
    private <T> Optional<T> getValueFromAnySettingsType(
            final String jsonData,
            final String path,
            final Class<T> resultType) {
        T result = null;
        try {
            JsonNode parent = mapper.readTree(jsonData);

            LOG.debug("Parsing transient settings for '{}'.", path);
            Optional<JsonNode> valueNode = getJsonValueNode(parent, TRANSIENT_SETTINGS_PATH_PREFIX + path)
                    .or(() -> {
                        LOG.debug("Parsing persistent settings for '{}'.", path);
                        return getJsonValueNode(parent, PERSISTENT_SETTINGS_PATH_PREFIX + path);
                    }).or(() -> {
                        LOG.debug("Parsing default settings for '{}'.", path);
                        return getJsonValueNode(parent, DEFAULT_SETTINGS_PATH_PREFIX + path);
                    });

            if (valueNode.isPresent()) {
                result = mapper.treeToValue(valueNode.get(), resultType);
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
