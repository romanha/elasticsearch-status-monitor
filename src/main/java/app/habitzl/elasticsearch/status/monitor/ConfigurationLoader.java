package app.habitzl.elasticsearch.status.monitor;

/**
 * Loads all available configuration and makes them available for the whole application.
 */
public interface ConfigurationLoader {

	/**
	 * Loads all available configuration, including the options passed as CLI arguments.
	 */
	void load(String[] cliArguments);
}
