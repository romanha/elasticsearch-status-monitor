package app.habitzl.elasticsearch.status.monitor.tool.client;

import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A mapper for converting Elasticsearch responses to other formats.
 */
public interface ResponseMapper {
    /**
     * Gets the content of an ES response as String representation.
     */
    String getContentAsString(Response response) throws IOException;

    /**
     * Maps an ES response with a single data object to a map of key-value pairs.
     */
    Map<String, Object> toMap(Response response) throws IOException;

    /**
     * Maps an ES response with an array of data objects to a list of maps containing key-value pairs.
     */
    List<Map<String, Object>> toMaps(Response response) throws IOException;
}
