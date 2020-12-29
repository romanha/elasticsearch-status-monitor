package app.habitzl.elasticsearch.status.monitor.presentation;

import app.habitzl.elasticsearch.status.monitor.ReportGenerator;
import app.habitzl.elasticsearch.status.monitor.presentation.file.ReportFile;
import app.habitzl.elasticsearch.status.monitor.presentation.model.Example;
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
	private static final String TEMPLATE_FILE_NAME = "test.ftlh";

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
	public void generate() {
		createReport();
	}

	private void createReport() {
		try {
			Template template = configuration.getTemplate(TEMPLATE_FILE_NAME);
			writeReport(template);
		} catch (IOException e) {
			LOG.error("Could not load template '" + TEMPLATE_FILE_NAME + "'.", e);
		} catch (TemplateException e) {
			LOG.error("Failed to create report.", e);
		}
	}

	private void writeReport(final Template template) throws IOException, TemplateException {
		FileWriter fileWriter = new FileWriter(reportFile);
		template.process(createExampleDataModel(), fileWriter);
		fileWriter.close();
	}

	/**
	 * TODO delete
	 */
	private Map<String, Object> createExampleDataModel() {
		Example product = new Example();
		product.setName("Test Product");
		product.setUrl("Test URL");

		return Map.of(
				"user", "Roman",
				"latestProduct", product
		);
	}
}
