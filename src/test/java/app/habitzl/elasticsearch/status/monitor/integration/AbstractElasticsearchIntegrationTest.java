package app.habitzl.elasticsearch.status.monitor.integration;

import app.habitzl.elasticsearch.status.monitor.Bootstrapper;
import app.habitzl.elasticsearch.status.monitor.ExitCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.commons.util.StringUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * The base class for all integration tests.
 */
public abstract class AbstractElasticsearchIntegrationTest implements ElasticsearchIntegrationTest {
    private static final Logger LOG = LogManager.getLogger(AbstractElasticsearchIntegrationTest.class);

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "9200";

    private static final String REPORT_PATH = "reports/integration-tests";
    private static final String REPORT_LATEST_FOLDER = "latest";
    private static final String REPORT_FILE_NAME = "index.html";

    private final Map<String, String> additionalConfigurationOptions = new HashMap<>();
    private Integer receivedExitCode = null;
    private ReportAssertions reportAssertions = null;

    @BeforeEach
    void setup() {
        LOG.info("[TEST] Starting integration test for Elasticsearch version '{}'.", getElasticsearchVersion().getVersion());
        additionalConfigurationOptions.clear();
        receivedExitCode = null;
        reportAssertions = null;
    }

    @AfterEach
    void cleanupReportDirectory() {
        try {
            LOG.info("[TEST] Cleaning up report directory '{}'.", REPORT_PATH);

            if (Files.exists(Paths.get(REPORT_PATH, REPORT_LATEST_FOLDER, REPORT_FILE_NAME))) {
                Files.delete(Paths.get(REPORT_PATH, REPORT_LATEST_FOLDER, REPORT_FILE_NAME));
            }

            if (Files.exists(Paths.get(REPORT_PATH, REPORT_LATEST_FOLDER))) {
                Files.delete(Paths.get(REPORT_PATH, REPORT_LATEST_FOLDER));
            }

            if (Files.exists(Paths.get(REPORT_PATH))) {
                Files.delete(Paths.get(REPORT_PATH));
            }
        } catch (IOException e) {
            LOG.error("[TEST] Failed to cleanup report directory.", e);
        }
    }

    @Override
    public void startAnalysis() {
        List<String> options = prepareConfiguration();
        receivedExitCode = Bootstrapper.start(options.toArray(String[]::new));
        assertThat("Expected to receive any exit code.", receivedExitCode, notNullValue());
    }

    @Override
    public String getHost() {
        return DEFAULT_HOST;
    }

    @Override
    public String getPort() {
        return DEFAULT_PORT;
    }

    /**
     * Allows tests to add command line parameters.
     * <p>
     * It is not supported to override the host or port here. Use the {@link #getHost()} and {@link #getPort()} methods instead.
     * <p>
     * It is not supported to change the report path configuration.
     * This is because the integration test automatically cleans up generated report files.
     */
    protected void addConfigurationOption(final String optionKey, final @Nullable String value) {
        if (optionKey.equals("--host") || optionKey.equals("--port")) {
            throw new IllegalArgumentException("[TEST] Changing the endpoint is not supported for integration tests. Override getHost() or getPort() instead.");
        }

        if (optionKey.equals("--reportPath") || optionKey.equals("--skipArchiveReport")) {
            throw new IllegalArgumentException("[TEST] Changing the report file configuration is not supported for integration tests.");
        }

        additionalConfigurationOptions.put(optionKey, value);
    }

    protected void useCustomCredentials(final String username, final String password) {
        addConfigurationOption("--username", username);
        addConfigurationOption("--password", password);
    }

    protected void assertThatExitCodeEquals(final ExitCode expectedCode) {
        assertThat("Exit code does not match.", receivedExitCode, equalTo(expectedCode.value()));
    }

    protected ReportAssertions getReportAssertions() {
        if (Objects.isNull(reportAssertions)) {
            try {
                File reportFile = assertThatReportFileExists();
                reportAssertions = ReportAssertions.create(reportFile);
            } catch (Exception e) {
                LOG.error("[TEST] Failed to prepare report assertions.", e);
                fail("Failed to prepare report assertions.", e);
            }
        }

        return reportAssertions;
    }

    protected File assertThatReportFileExists() {
        Path expectedReportFilePath = Paths.get(REPORT_PATH, REPORT_LATEST_FOLDER, REPORT_FILE_NAME);
        assertThat("Report file does not exist.", Files.exists(expectedReportFilePath), is(true));
        return expectedReportFilePath.toFile();
    }

    protected void assertThatReportFileDoesNotExist() {
        Path expectedReportFilePath = Paths.get(REPORT_PATH, REPORT_LATEST_FOLDER, REPORT_FILE_NAME);
        assertThat("Report file does not exist.", Files.exists(expectedReportFilePath), is(false));
    }

    private List<String> prepareConfiguration() {
        Map<String, String> defaultOptions = Map.ofEntries(
                Map.entry("--host", getHost()),
                Map.entry("--port", getPort()),
                Map.entry("--reportPath", REPORT_PATH),
                Map.entry("--skipArchiveReport", "")
        );
        return Stream.of(defaultOptions.entrySet(), additionalConfigurationOptions.entrySet())
                .flatMap(Collection::stream)
                .flatMap(entry -> Stream.of(entry.getKey(), entry.getValue()))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }
}
