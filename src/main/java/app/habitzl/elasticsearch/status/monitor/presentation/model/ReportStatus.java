package app.habitzl.elasticsearch.status.monitor.presentation.model;

/**
 * All possible report states.
 */
public enum ReportStatus {

	/**
	 * The tool was able to successfully create a full report.
	 */
	SUCCESS,

	/**
	 * The tool was not able to create a full report.
	 */
	ABORTED,
}
