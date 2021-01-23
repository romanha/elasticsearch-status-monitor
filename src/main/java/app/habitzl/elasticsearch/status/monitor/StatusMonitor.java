package app.habitzl.elasticsearch.status.monitor;

/**
 * The status monitor is the single entry point of this tool's functionality.
 */
public interface StatusMonitor {

    // TODO return AnalysisReport to get exit code in Main
    /**
     * Creates a monitoring snapshot of the current state of the ES cluster.
     */
    void createSnapshot();
}
