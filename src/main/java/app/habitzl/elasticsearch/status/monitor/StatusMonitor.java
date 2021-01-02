package app.habitzl.elasticsearch.status.monitor;

/**
 * The status monitor is the single entry point of this tool's functionality.
 */
public interface StatusMonitor {

	/**
	 * Creates a monitoring snapshot of the current state of the ES cluster.
	 */
	void createSnapshot();
}
