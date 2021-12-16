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
import java.time.Clock;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

/**
 * A provider responsible for returning the archive file of the report to generate.
 * If the output directories do no exist yet, the provider will automatically create them in advance.
 */
public class ArchiveReportFileProvider implements Provider<File> {
    private static final Logger LOG = LogManager.getLogger(ArchiveReportFileProvider.class);

    static final String REPORT_FILE_NAME = "index.html";
    static final String TIMESTAMP_FILE_PATTERN = "yyyy-MM-dd HH-mm-ss";

    private final Clock clock;
    private final FileCreator fileCreator;
    private final StatusMonitorConfiguration configuration;

    private Path reportFilePath;

    @Inject
    public ArchiveReportFileProvider(
            final Clock clock,
            final FileCreator fileCreator,
            final StatusMonitorConfiguration configuration) {
        this.clock = clock;
        this.fileCreator = fileCreator;
        this.configuration = configuration;
    }

    @Override
    public File get() {
        File reportFile = null;
        if (!configuration.isSkippingArchiveReport()) {
            Optional<Path> reportDirectory =
                    Objects.nonNull(reportFilePath)
                            ? Optional.of(reportFilePath)
                            : createTimestampReportDirectory();
            reportFile = reportDirectory.map(dir -> dir.resolve(REPORT_FILE_NAME))
                    .map(Path::toFile)
                    .orElse(null);
        }

        return reportFile;
    }

    /**
     * Creates an overall report directory with a sub-directory of the current timestamp.
     */
    private Optional<Path> createTimestampReportDirectory() {
        try {
            String timestamp = getFormattedTimestamp();
            reportFilePath = fileCreator.create(Paths.get(configuration.getReportFilesPath(), timestamp));
            LOG.info("Created the report directory '{}'.", reportFilePath);
        } catch (final IOException e) {
            LOG.error("Failed to create report directory.", e);
            reportFilePath = null;
        }

        return Optional.ofNullable(reportFilePath);
    }

    /**
     * Gets the current timestamp in a format that is supported by Windows and Unix file names.
     */
    private String getFormattedTimestamp() {
        Instant now = clock.instant();
        return DateTimeFormatter.ofPattern(TIMESTAMP_FILE_PATTERN)
                .withZone(clock.getZone())
                .format(now);
    }
}
