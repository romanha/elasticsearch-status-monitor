package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DefaultTimeParserTest {

    private DefaultTimeParser sut;

    @BeforeEach
    void setUp() {
        sut = new DefaultTimeParser();
    }

    @ParameterizedTest
    @MethodSource(value = "stringToDuration")
    void parse_string_returnsDuration(final String duration, final Duration expectedDuration) {
        // Given
        // duration as parameter

        // When
        Duration result = sut.parse(duration);

        // Then
        assertThat(result, equalTo(expectedDuration));
    }

    /**
     * Note that the smallest supported time unit is seconds, so 1.5s still results in only 1s.
     */
    private static Stream<Arguments> stringToDuration() {
        return Stream.of(
                Arguments.of(null, Duration.ZERO),
                Arguments.of("", Duration.ZERO),
                Arguments.of("invalid", Duration.ZERO),
                Arguments.of("42x", Duration.ZERO),
                Arguments.of("s", Duration.ZERO),
                Arguments.of("m", Duration.ZERO),
                Arguments.of("1s", Duration.ofSeconds(1)),
                Arguments.of("1.5s", Duration.ofSeconds(1)),
                Arguments.of("60s", Duration.ofMinutes(1)),
                Arguments.of("1m", Duration.ofMinutes(1)),
                Arguments.of("1.75m", Duration.ofSeconds(105)),
                Arguments.of("5m", Duration.ofMinutes(5)),
                Arguments.of("30m", Duration.ofMinutes(30)),
                Arguments.of("90m", Duration.ofMinutes(90)),
                Arguments.of("1501m", Duration.ofMinutes(1501)),
                Arguments.of("1h", Duration.ofHours(1)),
                Arguments.of("1.25h", Duration.ofMinutes(75)),
                Arguments.of("12h", Duration.ofHours(12)),
                Arguments.of("27h", Duration.ofHours(27)),
                Arguments.of("1d", Duration.ofDays(1)),
                Arguments.of("1.5d", Duration.ofHours(36)),
                Arguments.of("142d", Duration.ofDays(142))
        );
    }
}