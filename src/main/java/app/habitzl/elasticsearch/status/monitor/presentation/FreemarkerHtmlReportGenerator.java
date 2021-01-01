package app.habitzl.elasticsearch.status.monitor.presentation;

import app.habitzl.elasticsearch.status.monitor.ReportGenerator;
import app.habitzl.elasticsearch.status.monitor.presentation.file.TemplateProcessor;
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
	public FreemarkerHtmlReportGenerator(
			final Configuration configuration,
			final TemplateProcessor templateProcessor) {
		this.configuration = configuration;
		this.templateProcessor = templateProcessor;
	}

	@Override
	public void generate(final Object dataModel) {
		createReport(dataModel);
	}

	private void createReport(final Object dataModel) {
		try {
			Template template = configuration.getTemplate(TEMPLATE_FILE_NAME);
			templateProcessor.processTemplate(template, createReportDataModel(dataModel));
		} catch (final IOException e) {
			LOG.error("Could not load template '" + TEMPLATE_FILE_NAME + "'.", e);
		}
	}

	private Map<String, Object> createReportDataModel(final Object dataModel) {
		return Map.of(TEMPLATE_DATA_MODEL_REFERENCE, dataModel);
	}
}
