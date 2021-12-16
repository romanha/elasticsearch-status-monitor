package app.habitzl.elasticsearch.status.monitor.tool.presentation.file;

import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import app.habitzl.elasticsearch.status.monitor.util.FileCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

/**
 * A provider responsible for returning the file of the report to generate.
 * If the output directories do no exist yet, the provider will automatically create them in advance.
 */
public class LatestReportFileProvider implements Provider<File> {
    private static final Logger LOG = LogManager.getLogger(LatestReportFileProvider.class);

    static final String REPORT_FILE_NAME = "index.html";
    static final String DIRECTORY_NAME = "latest";

    private final FileCreator fileCreator;
    private final StatusMonitorConfiguration configuration;

    private Path reportFilePath;

    @Inject
    public LatestReportFileProvider(
            final FileCreator fileCreator,
            final StatusMonitorConfiguration configuration) {
        this.fileCreator = fileCreator;
        this.configuration = configuration;
    }

    @Override
    public File get() {
        Optional<Path> reportDirectory =
                Objects.nonNull(reportFilePath)
                        ? Optional.of(reportFilePath)
                        : createLatestReportDirectory();
        return reportDirectory.map(dir -> dir.resolve(REPORT_FILE_NAME))
                .map(Path::toFile)
                .orElse(null);
    }

    /**
     * Creates an overall report directory with a sub-directory for the "latest" report.
     */
    private Optional<Path> createLatestReportDirectory() {
        try {
            reportFilePath = fileCreator.create(Paths.get(configuration.getReportFilesPath(), DIRECTORY_NAME));
            LOG.info("Created the report directory '{}'.", reportFilePath);
        } catch (final IOException e) {
            LOG.error("Failed to create report directory.", e);
            reportFilePath = null;
        }

        return Optional.ofNullable(reportFilePath);
    }
}
