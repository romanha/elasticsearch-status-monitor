package app.habitzl.elasticsearch.status.monitor.tool.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.Strings;

import java.time.Duration;

public class DefaultTimeParser implements TimeParser {
	private static final Logger LOG = LogManager.getLogger(DefaultTimeParser.class);

	@Override
	public Duration parse(final String durationString) {
		Duration duration;

		if (Strings.isNullOrEmpty(durationString)) {
			duration = Duration.ZERO;
		} else if (durationString.endsWith("s")) {
			duration = Duration.ofSeconds(getNumberWithoutSuffix(durationString));
		} else if (durationString.endsWith("m")) {
			duration = Duration.ofMinutes(getNumberWithoutSuffix(durationString));
		} else if (durationString.endsWith("h")) {
			duration = Duration.ofHours(getNumberWithoutSuffix(durationString));
		} else if (durationString.endsWith("d")) {
			duration = Duration.ofDays(getNumberWithoutSuffix(durationString));
		} else {
			duration = Duration.ZERO;
			LOG.warn("Could not find any valid time unit for duration '{}'.", durationString);
		}

		return duration;
	}

	private int getNumberWithoutSuffix(final String durationString) {
		int number = 0;
		String numberString = durationString.substring(0, durationString.length() - 1);
		try {
			number = Integer.parseInt(numberString);
		} catch (NumberFormatException e) {
			LOG.error("Could not parse number of duration '" + durationString + "'.", e);
		}

		return number;
	}
}
