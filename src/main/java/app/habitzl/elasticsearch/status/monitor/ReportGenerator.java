package app.habitzl.elasticsearch.status.monitor;

/**
 * Generates a report.
 */
public interface ReportGenerator {

	/**
	 * Generates a new report based on the provided data model.
	 */
	void generate(Object dataModel);
}
