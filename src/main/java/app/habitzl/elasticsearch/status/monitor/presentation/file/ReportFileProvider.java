package app.habitzl.elasticsearch.status.monitor.presentation.file;

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
import java.util.Optional;

/**
 * A provider responsible for returning the file of the report to generate.
 * If the output directories do no exist yet, the provider will automatically create them in advance.
 */
public class ReportFileProvider implements Provider<File> {
	private static final Logger LOG = LogManager.getLogger(ReportFileProvider.class);

	static final String REPORT_DIRECTORY_NAME = "Elasticsearch Status Reports";
	static final String REPORT_FILE_NAME = "report.html";
	static final String TIMESTAMP_FILE_PATTERN = "yyyy-MM-dd HH-mm-ss";

	private final Clock clock;
	private final FileCreator fileCreator;

	@Inject
	public ReportFileProvider(final Clock clock, final FileCreator fileCreator) {
		this.clock = clock;
		this.fileCreator = fileCreator;
	}

	@Override
	public File get() {
		Optional<Path> reportDirectory = createTimestampReportDirectory();
		return reportDirectory.map(dir -> dir.resolve(REPORT_FILE_NAME))
							  .map(Path::toFile)
							  .orElse(null);
	}

	/**
	 * Creates an overall report directory with a sub-directory of the current timestamp.
	 */
	private Optional<Path> createTimestampReportDirectory() {
		Path path;
		try {
			String timestamp = getFormattedTimestamp();
			path = fileCreator.create(Paths.get(REPORT_DIRECTORY_NAME, timestamp));
			LOG.info("Created the report directory '{}'.", path);
		} catch (IOException e) {
			LOG.error("Failed to create report directory.", e);
			path = null;
		}

		return Optional.ofNullable(path);
	}

	/**
	 * Gets the current timestamp in a format that is supported by Windows file names.
	 */
	private String getFormattedTimestamp() {
		Instant now = clock.instant();
		return DateTimeFormatter.ofPattern(TIMESTAMP_FILE_PATTERN)
								.withZone(clock.getZone())
								.format(now);
	}
}
