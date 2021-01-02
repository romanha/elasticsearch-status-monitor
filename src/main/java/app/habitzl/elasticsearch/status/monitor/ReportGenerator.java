package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.presentation.model.StatusReport;

/**
 * Generates a report.
 */
public interface ReportGenerator {

	/**
	 * Generates a new report based on the provided data model.
	 */
	void generate(StatusReport dataModel);
}
