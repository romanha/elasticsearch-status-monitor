package app.habitzl.elasticsearch.status.monitor.util.format;

import app.habitzl.elasticsearch.status.monitor.Clocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DayBasedTimeFormatterTest {

    private DayBasedTimeFormatter sut;

    @BeforeEach
    void setUp() {
        Clock clock = Clocks.fixedUTC();
        sut = new DayBasedTimeFormatter(clock);
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

    @ParameterizedTest
    @MethodSource(value = "timestampToString")
    void format_timestamp_returnsFormattedString(final Instant timestamp, final String expectedString) {
        // Given
        // timestamp as parameter

        // When
        String result = sut.format(timestamp);

        // Then
        assertThat(result, equalTo(expectedString));
    }

    private static Stream<Arguments> durationToString() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of(Duration.ZERO, "0d 00h 00min"),
                Arguments.of(Duration.ofSeconds(59), "0d 00h 00min"),
                Arguments.of(Duration.ofMinutes(1), "0d 00h 01min"),
                Arguments.of(Duration.ofMinutes(5), "0d 00h 05min"),
                Arguments.of(Duration.ofMinutes(30), "0d 00h 30min"),
                Arguments.of(Duration.ofMinutes(90), "0d 01h 30min"),
                Arguments.of(Duration.ofMinutes(1501), "1d 01h 01min"),
                Arguments.of(Duration.ofHours(1), "0d 01h 00min"),
                Arguments.of(Duration.ofHours(12), "0d 12h 00min"),
                Arguments.of(Duration.ofHours(27), "1d 03h 00min"),
                Arguments.of(Duration.ofDays(1), "1d 00h 00min"),
                Arguments.of(Duration.ofDays(142), "142d 00h 00min")
        );
    }

    private static Stream<Arguments> timestampToString() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of(Instant.EPOCH, "1970-01-01 00:00:00"),
                Arguments.of(Instant.parse("2021-03-31T22:15:30.42Z"), "2021-03-31 22:15:30")
        );
    }
}