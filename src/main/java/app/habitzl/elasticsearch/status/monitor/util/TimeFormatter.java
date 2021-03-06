package app.habitzl.elasticsearch.status.monitor.util;

import java.time.Duration;
import java.time.Instant;

/**
 * A formatter to print times in a universal and pretty way.
 */
public interface TimeFormatter {

    /**
     * Converts a Java duration into a general string representation.
     */
    String format(Duration duration);

    /**
     * Converts a Java timestamp into a general string representation.
     */
    String format(Instant timestamp);
}
