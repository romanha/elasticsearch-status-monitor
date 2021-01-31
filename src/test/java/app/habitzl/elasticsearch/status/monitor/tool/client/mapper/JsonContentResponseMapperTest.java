package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.elasticsearch.client.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonContentResponseMapperTest {

    private static final String TEST_DATA = "some test data";

    private JsonContentResponseMapper sut;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = mock(ObjectMapper.class);
        sut = new JsonContentResponseMapper(mapper);
    }

    @Test
    void getContentAsString_response_returnString() throws IOException {
        // Given
        Response response = prepareResponse();

        // When
        String result = sut.getContentAsString(response);

        // Then
        assertThat(result, equalTo(TEST_DATA));
    }

    @Test
    void toMap_response_returnMap() throws IOException {
        // Given
        Response response = prepareResponse();
        Map<String, String> map = prepareMapperForSingleMap();

        // When
        Map<String, Object> result = sut.toMap(response);

        // Then
        assertThat(result, equalTo(map));
    }

    @Test
    void toMaps_response_returnMaps() throws IOException {
        // Given
        Response response = prepareResponse();
        List<Map<String, String>> maps = prepareMapperForMultipleMaps();

        // When
        List<Map<String, Object>> result = sut.toMaps(response);

        // Then
        assertThat(result, equalTo(maps));
    }

    private Response prepareResponse() throws IOException {
        HttpEntity httpEntity = mock(HttpEntity.class);
        when(httpEntity.getContent()).thenReturn(testInputStream());
        Response response = mock(Response.class);
        when(response.getEntity()).thenReturn(httpEntity);
        return response;
    }

    private InputStream testInputStream() {
        return new ByteArrayInputStream(TEST_DATA.getBytes());
    }

    private Map<String, String> prepareMapperForSingleMap() throws IOException {
        Map<String, String> map = Map.of("key", "value");
        when(mapper.readValue(TEST_DATA, Map.class)).thenReturn(map);
        return map;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, String>> prepareMapperForMultipleMaps() throws IOException {
        Map<String, String> map1 = Map.of("key1", "value1");
        Map<String, String> map2 = Map.of("key2", "value2");
        Map<String, String>[] maps = new Map[]{map1, map2};
        when(mapper.readValue(TEST_DATA, Map[].class)).thenReturn(maps);
        return Arrays.asList(maps);
    }
}