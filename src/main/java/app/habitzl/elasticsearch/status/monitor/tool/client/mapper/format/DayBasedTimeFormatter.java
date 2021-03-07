package app.habitzl.elasticsearch.status.monitor.tool.client.mapper.format;

import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.TimeFormatter;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Implementation of the {@link TimeFormatter} that formats
 * <ul>
 *     <li>durations as {@code [days]d [hours]h [minutes]min}.</li>
 *     <li>timestamps as {@code yyyy-MM-dd HH-mm-ss}.</li>
 * </ul>
 */
public class DayBasedTimeFormatter implements TimeFormatter {
    private static final String DURATION_FORMAT = "%dd %02dh %02dmin";
    static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final Clock clock;

    @Inject
    public DayBasedTimeFormatter(final Clock clock) {
        this.clock = clock;
    }

    @Override
    public String format(final Duration duration) {
        return Optional.ofNullable(duration)
                       .map(this::mapDuration)
                       .orElse("");
    }

    @Override
    public String format(final Instant timestamp) {
        return Optional.ofNullable(timestamp)
                       .map(this::mapInstant)
                       .orElse("");
    }

    private String mapDuration(final Duration duration) {
        return String.format(
                DURATION_FORMAT,
                duration.toDays(),
                duration.toHoursPart(),
                duration.toMinutesPart()
        );
    }

    private String mapInstant(final Instant timestamp) {
        return DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT)
                                .withZone(clock.getZone())
                                .format(timestamp);
    }
}
