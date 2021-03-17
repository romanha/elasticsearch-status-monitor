package app.habitzl.elasticsearch.status.monitor.exploration;

import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

@Disabled
class ExplorationTest {

    public static final String JSON_MAP_DYNAMIC_KEYS = "{\n"
            + "\"nodes\": {\n"
            + "  \"node-id-1\": {\n"
            + "    \"name\": \"node-1\",\n"
            + "    \"ip\": \"127.0.0.1\",\n"
            + "    \"version\": \"6.8.13\",\n"
            + "    \"roles\": [\n"
            + "      \"master\",\n"
            + "      \"data\",\n"
            + "      \"ingest\"\n"
            + "    ],\n"
            + "    \"os\": {\n"
            + "      \"name\": \"Windows 10\",\n"
            + "      \"pretty_name\": \"Windows 10\",\n"
            + "      \"available_processors\": 4\n"
            + "    }\n"
            + "  },"
            + "  \"node-id-2\": {\n"
            + "    \"name\": \"node-2\",\n"
            + "    \"ip\": \"127.0.0.1\",\n"
            + "    \"version\": \"6.8.13\",\n"
            + "    \"roles\": [\n"
            + "      \"master\"\n"
            + "    ],\n"
            + "    \"os\": {\n"
            + "      \"name\": \"Windows 10\",\n"
            + "      \"pretty_name\": \"Windows 10\",\n"
            + "      \"available_processors\": 8\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "}";

    @Test
    void parseAllNodeIdsFromJsonMap() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = new JsonParser(mapper);

        JsonNode parent = mapper.readTree(JSON_MAP_DYNAMIC_KEYS);
        JsonNode nodes = parent.at("/nodes");

        System.out.println(nodes.toString());

        // JSON Pointer does not support searching for dynamic keys
        // Instead I should switch to Jayway JsonPath and use the expression $.*~ to list all keys
        Optional<String[]> allNodeIds = parser.getValueFromPath(nodes.toString(), "/*.~", String[].class);

        System.out.println(allNodeIds);

        fail("This is just an exploration test.");
    }
}