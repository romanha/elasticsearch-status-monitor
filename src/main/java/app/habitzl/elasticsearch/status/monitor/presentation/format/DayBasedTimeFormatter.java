package app.habitzl.elasticsearch.status.monitor.presentation.format;

import app.habitzl.elasticsearch.status.monitor.presentation.TimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

/**
 * Implementation of the {@link TimeFormatter} that formats in [days]:[hours]:[minutes].
 */
public class DayBasedTimeFormatter implements TimeFormatter {
	private static final Logger LOG = LogManager.getLogger(DayBasedTimeFormatter.class);
	private static final String DURATION_FORMAT = "%d:%02d:%02d";

	@Override
	public String format(final Duration duration) {
		return String.format(DURATION_FORMAT,
				duration.toDays(),
				duration.toHoursPart(),
				duration.toMinutesPart()
		);
	}
}
