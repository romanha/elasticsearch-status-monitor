package app.habitzl.elasticsearch.status.monitor.tool;

import app.habitzl.elasticsearch.status.monitor.StatusMonitor;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;

import javax.inject.Inject;

/**
 * A {@link StatusMonitor} that generates report files based on the current Elasticsearch cluster state.
 */
public class ReportBasedStatusMonitor implements StatusMonitor {

	private final StatusAnalyser analyser;
	private final ReportGenerator reportGenerator;

	@Inject
	public ReportBasedStatusMonitor(final StatusAnalyser analyser, final ReportGenerator reportGenerator) {
		this.analyser = analyser;
		this.reportGenerator = reportGenerator;
	}

	@Override
	public void createSnapshot() {
		AnalysisReport report = analyser.createReport();
		reportGenerator.generate(report);
	}
}
