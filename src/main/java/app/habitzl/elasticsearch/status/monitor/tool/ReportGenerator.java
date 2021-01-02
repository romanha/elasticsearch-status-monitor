package app.habitzl.elasticsearch.status.monitor.tool;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;

/**
 * Generates a report.
 */
public interface ReportGenerator {

	/**
	 * Generates a new report based on the provided data model.
	 */
	void generate(AnalysisReport report);
}
