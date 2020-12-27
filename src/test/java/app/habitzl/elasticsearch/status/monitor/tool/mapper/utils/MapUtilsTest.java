package app.habitzl.elasticsearch.status.monitor.tool.mapper.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Map;

import static app.habitzl.elasticsearch.status.monitor.tool.mapper.utils.MapUtils.ZERO_FLOAT;
import static app.habitzl.elasticsearch.status.monitor.tool.mapper.utils.MapUtils.ZERO_INTEGER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;

class MapUtilsTest {

	private static final String KNOWN_KEY = "key";
	private static final String UNKNOWN_KEY = "unknown key";
	private static final String STRING_VALUE = "string value";

	private SecureRandom random;

	@BeforeEach
	void setUp() {
		random = new SecureRandom();
	}

	@Test
	void getString_unknownKey_returnsEmpty() {
		// Given
		Map<String, Object> map = Map.of(KNOWN_KEY, STRING_VALUE);

		// When
		String result = MapUtils.getString(map, UNKNOWN_KEY);

		// Then
		assertThat(result, emptyString());
	}

	@Test
	void getString_knownKey_returnsValue() {
		// Given
		Map<String, Object> map = Map.of(KNOWN_KEY, STRING_VALUE);

		// When
		String result = MapUtils.getString(map, KNOWN_KEY);

		// Then
		assertThat(result, equalTo(STRING_VALUE));
	}

	@Test
	void getInteger_unknownKey_returnsZero() {
		// Given
		Map<String, Object> map = Map.of(KNOWN_KEY, random.nextInt());

		// When
		int result = MapUtils.getInteger(map, UNKNOWN_KEY);

		// Then
		assertThat(result, equalTo(ZERO_INTEGER));
	}

	@Test
	void getInteger_knownKey_returnsValue() {
		// Given
		Integer value = random.nextInt();
		Map<String, Object> map = Map.of(KNOWN_KEY, value.toString());

		// When
		int result = MapUtils.getInteger(map, KNOWN_KEY);

		// Then
		assertThat(result, equalTo(value));
	}

	@Test
	void getFloat_unknownKey_returnsZero() {
		// Given
		Map<String, Object> map = Map.of(KNOWN_KEY, random.nextFloat());

		// When
		float result = MapUtils.getFloat(map, UNKNOWN_KEY);

		// Then
		assertThat(result, equalTo(ZERO_FLOAT));
	}

	@Test
	void getFloat_knownKey_returnsValue() {
		// Given
		Float value = random.nextFloat();
		Map<String, Object> map = Map.of(KNOWN_KEY, value.toString());

		// When
		float result = MapUtils.getFloat(map, KNOWN_KEY);

		// Then
		assertThat(result, equalTo(value));
	}
}