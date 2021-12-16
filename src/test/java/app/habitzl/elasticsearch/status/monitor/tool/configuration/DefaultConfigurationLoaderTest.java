package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import app.habitzl.elasticsearch.status.monitor.AnalysisStartOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class DefaultConfigurationLoaderTest {

    private DefaultConfigurationLoader sut;
    private StatusMonitorConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new StatusMonitorConfiguration();
        CliOptions cliOptions = new CliOptions();
        sut = new DefaultConfigurationLoader(configuration, cliOptions);
    }

    @Test
    void load_oneInvalidOption_returnAnalysisNotPossible() {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .withShortOption(CliOptions.HOST_OPTION_SHORT, "1.2.3.4")
                .withLongOption("unknown")
                .withShortOption(CliOptions.PORT_OPTION_SHORT, "9999")
                .build();

        // When
        AnalysisStartOption result = sut.load(args);

        // Then
        assertThat(result, is(AnalysisStartOption.ANALYSIS_NOT_POSSIBLE));
    }

    @Test
    void load_noHelpOptions_returnAnalysisPossible() {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .withShortOption(CliOptions.HOST_OPTION_SHORT, "1.2.3.4")
                .withShortOption(CliOptions.PORT_OPTION_SHORT, "9999")
                .build();

        // When
        AnalysisStartOption result = sut.load(args);

        // Then
        assertThat(result, is(AnalysisStartOption.ANALYSIS_POSSIBLE));
    }

    @Test
    void load_containsHelpOption_returnAnalysisNotRequested() {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .withShortOption(CliOptions.HOST_OPTION_SHORT, "1.2.3.4")
                .withShortOption(CliOptions.PORT_OPTION_SHORT, "9999")
                .withLongOption(CliOptions.HELP_OPTION_LONG)
                .build();

        // When
        AnalysisStartOption result = sut.load(args);

        // Then
        assertThat(result, is(AnalysisStartOption.ANALYSIS_NOT_REQUESTED));
    }

    @Test
    void load_containsVersionOption_returnAnalysisNotRequested() {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .withShortOption(CliOptions.HOST_OPTION_SHORT, "1.2.3.4")
                .withShortOption(CliOptions.PORT_OPTION_SHORT, "9999")
                .withLongOption(CliOptions.VERSION_OPTION_LONG)
                .build();

        // When
        AnalysisStartOption result = sut.load(args);

        // Then
        assertThat(result, is(AnalysisStartOption.ANALYSIS_NOT_REQUESTED));
    }

    @Test
    void load_validShortOptions_setConfiguration() {
        // Given
        String address = "1.2.3.4";
        String port = "9999";
        String[] args = ArgumentBuilder
                .create()
                .withShortOption(CliOptions.HOST_OPTION_SHORT, address)
                .withShortOption(CliOptions.PORT_OPTION_SHORT, port)
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.getHost(), equalTo(address));
        assertThat(configuration.getPort(), equalTo(port));
    }

    @Test
    void load_validLongOptions_setConfiguration() {
        // Given
        String address = "1.2.3.4";
        String port = "9999";
        String username = "username";
        String password = "password";
        String fallbackEndpoint1 = "2.2.2.2:9200";
        String fallbackEndpoint2 = "3.3.3.3:9202";
        String fallbackEndpoints = fallbackEndpoint1 + "," + fallbackEndpoint2;
        String reportFilesPath = "reports/custom";
        String[] args = ArgumentBuilder
                .create()
                .withLongOption(CliOptions.HOST_OPTION_LONG, address)
                .withLongOption(CliOptions.PORT_OPTION_LONG, port)
                .withLongOption(CliOptions.USER_OPTION_LONG, username)
                .withLongOption(CliOptions.PASSWORD_OPTION_LONG, password)
                .withLongOption(CliOptions.FALLBACK_ENDPOINTS_OPTION_LONG, fallbackEndpoints)
                .withLongOption(CliOptions.REPORT_FILES_PATH_OPTION_LONG, reportFilesPath)
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.getHost(), equalTo(address));
        assertThat(configuration.getPort(), equalTo(port));
        assertThat(configuration.getUsername(), equalTo(username));
        assertThat(configuration.getPassword(), equalTo(password));
        assertThat(configuration.getFallbackEndpoints(), contains(fallbackEndpoint1, fallbackEndpoint2));
        assertThat(configuration.getReportFilesPath(), equalTo(reportFilesPath));
    }

    @Test
    void load_oneValidOption_setConfigurationForOnlyThisOption() {
        // Given
        String address = "1.2.3.4";
        String[] args = ArgumentBuilder
                .create()
                .withShortOption(CliOptions.HOST_OPTION_SHORT, address)
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.getHost(), equalTo(address));
        assertThat(configuration.getPort(), equalTo(StatusMonitorConfiguration.DEFAULT_PORT));
        assertThat(configuration.isUsingHttps(), equalTo(StatusMonitorConfiguration.DEFAULT_USING_HTTPS));
        assertThat(configuration.getUsername(), equalTo(StatusMonitorConfiguration.DEFAULT_USERNAME));
        assertThat(configuration.getPassword(), equalTo(StatusMonitorConfiguration.DEFAULT_PASSWORD));
        assertThat(configuration.getReportFilesPath(), equalTo(StatusMonitorConfiguration.DEFAULT_REPORT_FILES_PATH));
        assertThat(configuration.isSkippingArchiveReport(), equalTo(StatusMonitorConfiguration.DEFAULT_SKIP_ARCHIVE_REPORT));
    }

    @Test
    void load_oneInvalidOption_useDefaultConfiguration() {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .withShortOption(CliOptions.HOST_OPTION_SHORT, "1.2.3.4")
                .withLongOption("unknown")
                .withShortOption(CliOptions.PORT_OPTION_SHORT, "9999")
                .build();

        // When
        sut.load(args);

        // Then
        assertThatConfigurationIsDefault();
    }

    @Test
    void load_noOptions_useDefaultConfiguration() {
        // Given
        String[] args = {};

        // When
        sut.load(args);

        // Then
        assertThatConfigurationIsDefault();
    }

    @Test
    void load_includingBlankFallbackEndpoints_ignoreBlankFallbackEndpoints() {
        // Given
        String validEndpoint = "127.0.0.1:9200";
        String[] args = ArgumentBuilder
                .create()
                .withLongOption(CliOptions.FALLBACK_ENDPOINTS_OPTION_LONG, ",, , " + validEndpoint + "  ,,")
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.getFallbackEndpoints(), contains(validEndpoint));
    }

    @Test
    void load_noUnsecureOption_enableSecurity() {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.isUsingHttps(), equalTo(true));
    }

    @Test
    void load_unsecureOption_disableSecurity() {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .withLongOption(CliOptions.UNSECURE_OPTION_LONG)
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.isUsingHttps(), equalTo(false));
    }

    @ParameterizedTest
    @MethodSource("validRelativeFilePaths")
    void load_validRelativeReportFilesPath_usePath(final String validPath) {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .withLongOption(CliOptions.REPORT_FILES_PATH_OPTION_LONG, validPath)
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.getReportFilesPath(), equalTo(validPath));
    }

    @EnabledOnOs(OS.WINDOWS)
    @ParameterizedTest
    @MethodSource("validAbsoluteFilePathsWindows")
    void load_validAbsoluteReportFilesPathOnWindows_usePath(final String validPath) {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .withLongOption(CliOptions.REPORT_FILES_PATH_OPTION_LONG, validPath)
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.getReportFilesPath(), equalTo(validPath));
    }

    @EnabledOnOs(OS.WINDOWS)
    @ParameterizedTest
    @MethodSource("invalidFilePathsWindows")
    void load_invalidReportFilesPathOnWindows_useDefaultPath(final String invalidPath) {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .withLongOption(CliOptions.REPORT_FILES_PATH_OPTION_LONG, invalidPath)
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.getReportFilesPath(), equalTo(StatusMonitorConfiguration.DEFAULT_REPORT_FILES_PATH));
    }

    @Test
    void load_noSkipArchiveReportOption_disableSkippingArchiveReport() {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.isSkippingArchiveReport(), equalTo(false));
    }

    @Test
    void load_skipArchiveReportOption_enableSkippingArchiveReport() {
        // Given
        String[] args = ArgumentBuilder
                .create()
                .withLongOption(CliOptions.SKIP_ARCHIVE_REPORT_LONG)
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.isSkippingArchiveReport(), equalTo(true));
    }

    private void assertThatConfigurationIsDefault() {
        assertThat(configuration, equalTo(StatusMonitorConfiguration.defaultConfig()));
    }

    private static final class ArgumentBuilder {
        private final List<String> arguments;

        private static ArgumentBuilder create() {
            return new ArgumentBuilder();
        }

        private ArgumentBuilder() {
            arguments = new ArrayList<>();
        }

        private ArgumentBuilder withShortOption(final String option) {
            arguments.add("-" + option);
            return this;
        }

        private ArgumentBuilder withShortOption(final String option, final String value) {
            arguments.add("-" + option);
            arguments.add(value);
            return this;
        }

        private ArgumentBuilder withLongOption(final String option) {
            arguments.add("--" + option);
            return this;
        }

        private ArgumentBuilder withLongOption(final String option, final String value) {
            arguments.add("--" + option);
            arguments.add(value);
            return this;
        }

        private String[] build() {
            return arguments.toArray(new String[0]);
        }
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> validRelativeFilePaths() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("reports"),
                Arguments.of("reports" + File.separator + "valid" + File.separator + "path")
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> validAbsoluteFilePathsWindows() {
        return Stream.of(
                Arguments.of("C:\\ProgramData\\Temp")
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> invalidFilePathsWindows() {
        return Stream.of(
                Arguments.of("reports:invalid"),
                Arguments.of("reports?invalid"),
                Arguments.of("reports\"invalid"),
                Arguments.of("reports<invalid"),
                Arguments.of("reports>invalid"),
                Arguments.of("reports|invalid")
        );
    }
}