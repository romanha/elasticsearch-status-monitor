package app.habitzl.elasticsearch.status.monitor.tool.presentation.file;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Processes a FreeMarker {@link Template}.
 * This is used to automatically create and close the required file writer.
 */
public class TemplateProcessor {
	private static final Logger LOG = LogManager.getLogger(TemplateProcessor.class);

	private final File reportFile;

	@Inject
	public TemplateProcessor(final @ReportFile File reportFile) {
		this.reportFile = reportFile;
	}

	public void processTemplate(final Template template, final Object dataModel) {
		try (FileWriter fileWriter = new FileWriter(reportFile)) {
			template.process(dataModel, fileWriter);
		} catch (final IOException | NullPointerException e) {
			LOG.error("Failed to create file writer for report file '" + reportFile + "'.", e);
		} catch (final TemplateException e) {
			LOG.error("Failed to create report.", e);
		}
	}
}
