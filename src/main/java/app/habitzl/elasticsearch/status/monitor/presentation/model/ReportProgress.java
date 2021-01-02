package app.habitzl.elasticsearch.status.monitor.presentation.model;

/**
 * All possible report progress states.
 */
public enum ReportProgress {

	/**
	 * The tool was able to successfully create a full report.
	 */
	FINISHED,

	/**
	 * The tool was not able to create a full report.
	 */
	ABORTED,
}
