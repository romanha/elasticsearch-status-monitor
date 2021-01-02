package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import java.time.Duration;

/**
 * Parses strings representing times and durations into Java objects.
 */
public interface TimeParser {

	/**
	 * Converts an ES duration string into a Java duration.
	 *
	 * @see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/common-options.html#time-units">Elasticsearch time units documentation</a>
	 */
	Duration parse(String durationString);
}
