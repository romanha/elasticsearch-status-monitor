package app.habitzl.elasticsearch.status.monitor.tool.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.ResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JsonContentResponseMapper implements ResponseMapper {

	private final ObjectMapper mapper;

	@Inject
	public JsonContentResponseMapper(final ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> toMap(final Response response) throws IOException {
		String body = getContent(response);
		return mapper.readValue(body, Map.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> toMaps(final Response response) throws IOException {
		String body = getContent(response);
		return Arrays.asList(mapper.readValue(body, Map[].class));
	}

	private String getContent(final Response response) throws IOException {
		return EntityUtils.toString(response.getEntity());
	}
}
