package app.habitzl.elasticsearch.status.monitor.presentation.format;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DayBasedTimeFormatterTest {

	private DayBasedTimeFormatter sut;

	@BeforeEach
	void setUp() {
		sut = new DayBasedTimeFormatter();
	}

	@ParameterizedTest
	@MethodSource(value = "durationToString")
	void format_duration_returnsFormattedString(final Duration duration, final String expectedString) {
		// Given
		// duration as parameter

		// When
		String result = sut.format(duration);

		// Then
		assertThat(result, equalTo(expectedString));
	}

	private static Stream<Arguments> durationToString() {
		return Stream.of(
				Arguments.of(Duration.ZERO, "0:00:00"),
				Arguments.of(Duration.ofSeconds(59), "0:00:00"),
				Arguments.of(Duration.ofMinutes(1), "0:00:01"),
				Arguments.of(Duration.ofMinutes(5), "0:00:05"),
				Arguments.of(Duration.ofMinutes(30), "0:00:30"),
				Arguments.of(Duration.ofMinutes(90), "0:01:30"),
				Arguments.of(Duration.ofMinutes(1501), "1:01:01"),
				Arguments.of(Duration.ofHours(1), "0:01:00"),
				Arguments.of(Duration.ofHours(12), "0:12:00"),
				Arguments.of(Duration.ofHours(27), "1:03:00"),
				Arguments.of(Duration.ofDays(1), "1:00:00"),
				Arguments.of(Duration.ofDays(142), "142:00:00")
		);
	}
}