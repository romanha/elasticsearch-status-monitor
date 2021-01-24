package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;

/**
 * The status monitor is the single entry point of this tool's functionality.
 */
public interface StatusMonitor {

    /**
     * Creates a monitoring snapshot of the current state of the ES cluster.
     *
     * @return The report data of the monitored snapshot.
     */
    AnalysisReport createSnapshot();
}
