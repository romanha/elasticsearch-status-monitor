package app.habitzl.elasticsearch.status.monitor.mapper;

import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public interface ResponseMapper {
	Map<String, Object> toMap(Response response) throws IOException;

	List<Map<String, Object>> toMaps(Response response) throws IOException;
}
