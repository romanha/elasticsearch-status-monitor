package app.habitzl.elasticsearch.status.monitor;

/**
 * TODO
 */
public interface StatusMonitor {

	/**
	 * Creates a monitoring snapshot of the current state of the ES cluster.
	 */
	void createSnapshot();
}
