package app.habitzl.elasticsearch.status.monitor.tool.client.mapper.format;

import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.TimeFormatter;
import java.time.Duration;

/**
 * Implementation of the {@link TimeFormatter} that formats time as {@code [days]d [hours]h [minutes]min}.
 */
public class DayBasedTimeFormatter implements TimeFormatter {
    private static final String DURATION_FORMAT = "%dd %02dh %02dmin";

    @Override
    public String format(final Duration duration) {
        return String.format(
                DURATION_FORMAT,
                duration.toDays(),
                duration.toHoursPart(),
                duration.toMinutesPart()
        );
    }
}
