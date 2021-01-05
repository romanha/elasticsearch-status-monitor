package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import com.google.common.base.Strings;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultTimeParser implements TimeParser {
    private static final Logger LOG = LogManager.getLogger(DefaultTimeParser.class);

    @Override
    public Duration parse(final String durationString) {
        Duration duration;

        if (Strings.isNullOrEmpty(durationString)) {
            duration = Duration.ZERO;
        } else if (durationString.endsWith("s")) {
            float number = getFloatWithoutSuffix(durationString);
            duration = Duration.ofSeconds((int) number);
        } else if (durationString.endsWith("m")) {
            float number = getFloatWithoutSuffix(durationString);
            duration = Duration.ofSeconds((int) (number * 60));
        } else if (durationString.endsWith("h")) {
            float number = getFloatWithoutSuffix(durationString);
            duration = Duration.ofSeconds((int) (number * 60 * 60));
        } else if (durationString.endsWith("d")) {
            float number = getFloatWithoutSuffix(durationString);
            duration = Duration.ofSeconds((int) (number * 60 * 60 * 24));
        } else {
            duration = Duration.ZERO;
            LOG.warn("Could not find any valid time unit for duration '{}'.", durationString);
        }

        return duration;
    }

    private float getFloatWithoutSuffix(final String durationString) {
        float number = 0.0f;
        String numberString = durationString.substring(0, durationString.length() - 1);
        try {
            number = Float.parseFloat(numberString);
        } catch (final NumberFormatException e) {
            LOG.error("Could not parse float number of duration '" + durationString + "'.", e);
        }

        return number;
    }
}
