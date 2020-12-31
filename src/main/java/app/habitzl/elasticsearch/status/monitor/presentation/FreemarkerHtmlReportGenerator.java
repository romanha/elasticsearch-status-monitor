package app.habitzl.elasticsearch.status.monitor.presentation;

import app.habitzl.elasticsearch.status.monitor.ReportGenerator;
import app.habitzl.elasticsearch.status.monitor.presentation.file.ReportFile;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * A {@link ReportGenerator} based on the Apache FreeMarker template engine that creates HTML reports.
 */
public class FreemarkerHtmlReportGenerator implements ReportGenerator {
	private static final Logger LOG = LogManager.getLogger(FreemarkerHtmlReportGenerator.class);
	static final String TEMPLATE_FILE_NAME = "report.ftlh";

	private final Configuration configuration;
	private final File reportFile;

	@Inject
	public FreemarkerHtmlReportGenerator(
			final Configuration configuration,
			final @ReportFile File reportFile) {
		this.configuration = configuration;
		this.reportFile = reportFile;
	}

	@Override
	public void generate(final Object dataModel) {
		createReport(dataModel);
	}

	private void createReport(final Object dataModel) {
		try {
			Template template = configuration.getTemplate(TEMPLATE_FILE_NAME);
			writeReport(template, dataModel);
		} catch (IOException e) {
			LOG.error("Could not load template '" + TEMPLATE_FILE_NAME + "'.", e);
		} catch (TemplateException e) {
			LOG.error("Failed to create report.", e);
		}
	}

	private void writeReport(final Template template, final Object dataModel) throws IOException, TemplateException {
		FileWriter fileWriter = new FileWriter(reportFile);
		template.process(createReportDataModel(dataModel), fileWriter);
		fileWriter.close();
	}

	private Map<String, Object> createReportDataModel(final Object dataModel) {
		return Map.of("report", dataModel);
	}
}
