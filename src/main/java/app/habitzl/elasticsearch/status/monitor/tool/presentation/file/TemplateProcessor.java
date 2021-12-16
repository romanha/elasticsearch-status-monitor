package app.habitzl.elasticsearch.status.monitor.tool.presentation.file;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * Processes a FreeMarker {@link Template}.
 * This is used to automatically create and close the required file writer.
 */
public class TemplateProcessor {
    private static final Logger LOG = LogManager.getLogger(TemplateProcessor.class);

    private final File reportFile;
    private final @Nullable File archiveFile;

    @Inject
    public TemplateProcessor(final @ReportFile File reportFile, final @ArchiveReportFile @Nullable File archiveFile) {
        this.reportFile = reportFile;
        this.archiveFile = archiveFile;
    }

    public void processTemplate(final Template template, final Object dataModel) {
        writeReport(template, dataModel);
        writeArchiveReport(template, dataModel);
    }

    private void writeReport(final Template template, final Object dataModel) {
        try (FileWriter fileWriter = new FileWriter(reportFile)) {
            template.process(dataModel, fileWriter);
        } catch (final IOException | NullPointerException e) {
            LOG.error("Failed to create file writer for report file '" + reportFile + "'.", e);
        } catch (final TemplateException e) {
            LOG.error("Failed to create report.", e);
        }
    }

    private void writeArchiveReport(final Template template, final Object dataModel) {
        if (Objects.nonNull(archiveFile)) {
            try (FileWriter fileWriter = new FileWriter(archiveFile)) {
                template.process(dataModel, fileWriter);
            } catch (final IOException | NullPointerException e) {
                LOG.error("Failed to create file writer for report file '" + archiveFile + "'.", e);
            } catch (final TemplateException e) {
                LOG.error("Failed to create report.", e);
            }
        }
    }
}
