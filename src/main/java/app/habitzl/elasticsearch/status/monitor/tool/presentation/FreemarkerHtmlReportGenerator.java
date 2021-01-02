package app.habitzl.elasticsearch.status.monitor.tool.presentation;

import app.habitzl.elasticsearch.status.monitor.tool.ReportGenerator;
import app.habitzl.elasticsearch.status.monitor.tool.presentation.file.TemplateProcessor;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

/**
 * A {@link ReportGenerator} based on the Apache FreeMarker template engine that creates HTML reports.
 */
public class FreemarkerHtmlReportGenerator implements ReportGenerator {
	private static final Logger LOG = LogManager.getLogger(FreemarkerHtmlReportGenerator.class);
	static final String TEMPLATE_FILE_NAME = "report.ftlh";
	static final String TEMPLATE_DATA_MODEL_REFERENCE = "report";

	private final Configuration configuration;
	private final TemplateProcessor templateProcessor;

	@Inject
	public FreemarkerHtmlReportGenerator(final Configuration configuration, final TemplateProcessor templateProcessor) {
		this.configuration = configuration;
		this.templateProcessor = templateProcessor;
	}

	@Override
	public void generate(final AnalysisReport dataModel) {
		createReport(dataModel);
	}

	private void createReport(final AnalysisReport report) {
		try {
			Template template = configuration.getTemplate(TEMPLATE_FILE_NAME);
			templateProcessor.processTemplate(template, createReportDataModel(report));
		} catch (final IOException e) {
			LOG.error("Could not load template '" + TEMPLATE_FILE_NAME + "'.", e);
		}
	}

	private Map<String, AnalysisReport> createReportDataModel(final AnalysisReport report) {
		return Map.of(TEMPLATE_DATA_MODEL_REFERENCE, report);
	}
}
