package app.habitzl.elasticsearch.status.monitor.presentation;

import java.time.Duration;

/**
 * A formatter to print times in a universal and pretty way.
 */
public interface TimeFormatter {

	/**
	 * Converts a Java duration into a general string representation.
	 */
	String format(Duration duration);
}
