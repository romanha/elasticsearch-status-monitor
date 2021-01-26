package app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAnd;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
}