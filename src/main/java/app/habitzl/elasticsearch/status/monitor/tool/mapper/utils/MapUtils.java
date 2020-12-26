package app.habitzl.elasticsearch.status.monitor.tool.mapper.utils;

import com.google.common.base.Strings;

import java.util.Map;
import java.util.Objects;

/**
 * A utility class for map operations.
 */
public final class MapUtils {

	private static final String EMPTY_STRING = "";

	private MapUtils() {
		// instantiation protection
	}

	public static String getString(final Map<String, Object> data, final String key) {
		Object value = data.get(key);
		return Objects.isNull(value) ? EMPTY_STRING : (String) value;
	}

	public static int getInteger(final Map<String, Object> data, final String key) {
		String stringValue = getString(data, key);
		return Strings.isNullOrEmpty(stringValue) ? 0 : Integer.parseInt(stringValue);
	}

	public static float getFloat(final Map<String, Object> data, final String key) {
		String stringValue = getString(data, key);
		return Strings.isNullOrEmpty(stringValue) ? 0.0f : Float.parseFloat(stringValue);
	}
}
