package app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAnd;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class JsonParserTest {

    private static final String TEST_CHILD_STRING_VALUE = "test child string";
    private static final String TEST_STRING_VALUE = "test string";
    private static final int TEST_NUMBER_VALUE = 42;

    private static final String JSON_TEST_STRING = "{\n"
            + "  \"parent\": {\n"
            + "    \"childStringValue\": \"" + TEST_CHILD_STRING_VALUE + "\"\n"
            + "  },\n"
            + "  \"stringValue\": \"" + TEST_STRING_VALUE + "\",\n"
            + "  \"numberValue\": " + TEST_NUMBER_VALUE + ",\n"
            + "  \"emptyJsonObject\": {}\n"
            + "}";
    private static final String JSON_TEST_LIST = "{\n"
            + "  \"stringValue\": \"someValue\",\n"
            + "  \"list\": [\n"
            + "    {"
            + "      \"stringValue\": \"" + TEST_STRING_VALUE + "\",\n"
            + "      \"numberValue\": \"" + TEST_NUMBER_VALUE + "\"\n"
            + "    },\n"
            + "    {"
            + "      \"stringValue\": \"" + (TEST_STRING_VALUE + "-1") + "\",\n"
            + "      \"numberValue\": \"" + (TEST_NUMBER_VALUE + 100) + "\"\n"
            + "    },\n"
            + "    {"
            + "      \"stringValue\": \"" + (TEST_STRING_VALUE + "-2") + "\",\n"
            + "      \"numberValue\": \"" + (TEST_NUMBER_VALUE + 200) + "\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    private JsonParser sut;

    @BeforeEach
    void setUp() {
        sut = new JsonParser(new ObjectMapper());
    }

    @Test
    void getValueFromPath_invalidJsonInput_returnEmpty() {
        // Given
        String invalidJson = "{\n"
                + "  \"stringValue\": \"" + TEST_STRING_VALUE + "\",\n"
                + "  \"invalid\",\n"
                + "}";
        String path = "/stringValue";

        // When
        Optional<String> result = sut.getValueFromPath(invalidJson, path, String.class);

        // Then
        assertThat(result, isEmpty());
    }

    @Test
    void getValueFromPath_invalidPath_returnEmpty() {
        // Given
        String path = "/invalid/path";

        // When
        Optional<String> result = sut.getValueFromPath(JSON_TEST_STRING, path, String.class);

        // Then
        assertThat(result, isEmpty());
    }

    @Test
    void getValueFromPath_incompatibleValueType_returnEmpty() {
        // Given
        String path = "/stringValue";

        // When
        Optional<Integer> result = sut.getValueFromPath(JSON_TEST_STRING, path, Integer.class);

        // Then
        assertThat(result, isEmpty());
    }

    @Test
    void getValueFromPath_emptyJsonObject_returnEmpty() {
        // Given
        String path = "/emptyJsonObject";

        // When
        Optional<Object> result = sut.getValueFromPath(JSON_TEST_STRING, path, Object.class);

        // Then
        assertThat(result, isEmpty());
    }

    @Test
    void getValueFromPath_stringValueType_returnString() {
        // Given
        String path = "/stringValue";

        // When
        Optional<String> result = sut.getValueFromPath(JSON_TEST_STRING, path, String.class);

        // Then
        assertThat(result, isPresentAnd(equalTo(TEST_STRING_VALUE)));
    }

    @Test
    void getValueFromPath_integerValueType_returnInteger() {
        // Given
        String path = "/numberValue";

        // When
        Optional<Integer> result = sut.getValueFromPath(JSON_TEST_STRING, path, Integer.class);

        // Then
        assertThat(result, isPresentAnd(equalTo(TEST_NUMBER_VALUE)));
    }

    @Test
    void getValueFromPath_complexPathWithStringValueType_returnString() {
        // Given
        String path = "/parent/childStringValue";

        // When
        Optional<String> result = sut.getValueFromPath(JSON_TEST_STRING, path, String.class);

        // Then
        assertThat(result, isPresentAnd(equalTo(TEST_CHILD_STRING_VALUE)));
    }

    @Test
    void getValueFromJsonObject_objectNodeWithNumberValue_returnValue() {
        // Given
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode()
                                                        .put("numberValue", TEST_NUMBER_VALUE);

        // When
        Optional<Integer> result = sut.getValueFromJsonObject(objectNode, "/numberValue", Integer.class);

        // Then
        assertThat(result, isPresentAnd(equalTo(TEST_NUMBER_VALUE)));
    }

    @Test
    void getValueFromJsonObject_objectNodeWithStringValue_returnValue() {
        // Given
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode()
                                                        .put("stringValue", TEST_STRING_VALUE);

        // When
        Optional<String> result = sut.getValueFromJsonObject(objectNode, "/stringValue", String.class);

        // Then
        assertThat(result, isPresentAnd(equalTo(TEST_STRING_VALUE)));
    }

    @Test
    void getValueFromJsonObject_objectNodeWithMismatchingType_returnEmpty() {
        // Given
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode()
                                                        .put("stringValue", TEST_STRING_VALUE);

        // When
        Optional<Integer> result = sut.getValueFromJsonObject(objectNode, "/stringValue", Integer.class);

        // Then
        assertThat(result, isEmpty());
    }

    @Test
    void getValueFromJsonObject_notObjectNode_returnEmpty() {
        // Given
        ValueNode valueNode = JsonNodeFactory.instance.numberNode(TEST_NUMBER_VALUE);

        // When
        Optional<Integer> result = sut.getValueFromJsonObject(valueNode, "/", Integer.class);

        // Then
        assertThat(result, isEmpty());
    }

    @Test
    void getListFromPath_pathContainsArrayElements_returnListOfNodes() {
        // Given
        String path = "/list";

        // When
        List<JsonNode> result = sut.getListFromPath(JSON_TEST_LIST, path);

        // Then
        assertThat(result, hasSize(3));
    }

    @Test
    void getListFromPath_pathDoesNotContainArrayElements_returnEmptyList() {
        // Given
        String path = "/notList";

        // When
        List<JsonNode> result = sut.getListFromPath(JSON_TEST_LIST, path);

        // Then
        assertThat(result, empty());
    }

    @Test
    void getListFromPath_invalidPath_returnEmptyList() {
        // Given
        String path = "/invalid/path";

        // When
        List<JsonNode> result = sut.getListFromPath(JSON_TEST_LIST, path);

        // Then
        assertThat(result, empty());
    }
}