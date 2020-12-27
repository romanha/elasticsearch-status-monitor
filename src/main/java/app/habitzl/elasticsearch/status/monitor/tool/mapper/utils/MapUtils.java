package app.habitzl.elasticsearch.status.monitor.tool.mapper.utils;

import com.google.common.base.Strings;

import java.util.Map;
import java.util.Objects;

/**
 * A utility class for map operations.
 */
public final class MapUtils {

	static final String EMPTY_STRING = "";
	static final int ZERO_INTEGER = 0;
	static final float ZERO_FLOAT = 0.0f;

	private MapUtils() {
		// instantiation protection
	}

	public static String getString(final Map<String, Object> data, final String key) {
		Object value = data.get(key);
		return Objects.isNull(value) ? EMPTY_STRING : (String) value;
	}

	public static int getInteger(final Map<String, Object> data, final String key) {
		String stringValue = getString(data, key);
		return Strings.isNullOrEmpty(stringValue) ? ZERO_INTEGER : Integer.parseInt(stringValue);
	}

	public static float getFloat(final Map<String, Object> data, final String key) {
		String stringValue = getString(data, key);
		return Strings.isNullOrEmpty(stringValue) ? ZERO_FLOAT : Float.parseFloat(stringValue);
	}
}
