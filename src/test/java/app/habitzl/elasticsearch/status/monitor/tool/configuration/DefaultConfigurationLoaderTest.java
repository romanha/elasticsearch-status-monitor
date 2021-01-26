package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import app.habitzl.elasticsearch.status.monitor.AnalysisStartOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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
        String[] args = ArgumentBuilder
                .create()
                .withLongOption(CliOptions.HOST_OPTION_LONG, address)
                .withLongOption(CliOptions.PORT_OPTION_LONG, port)
                .withLongOption(CliOptions.USER_OPTION_LONG, username)
                .withLongOption(CliOptions.PASSWORD_OPTION_LONG, password)
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.getHost(), equalTo(address));
        assertThat(configuration.getPort(), equalTo(port));
        assertThat(configuration.getUsername(), equalTo(username));
        assertThat(configuration.getPassword(), equalTo(password));
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
                .withShortOption(CliOptions.UNSECURE_OPTION_LONG)
                .build();

        // When
        sut.load(args);

        // Then
        assertThat(configuration.isUsingHttps(), equalTo(false));
    }

    private void assertThatConfigurationIsDefault() {
        assertThat(configuration.getHost(), equalTo(StatusMonitorConfiguration.DEFAULT_HOST));
        assertThat(configuration.getPort(), equalTo(StatusMonitorConfiguration.DEFAULT_PORT));
        assertThat(configuration.isUsingHttps(), equalTo(StatusMonitorConfiguration.DEFAULT_USING_HTTPS));
        assertThat(configuration.getUsername(), equalTo(StatusMonitorConfiguration.DEFAULT_USERNAME));
        assertThat(configuration.getPassword(), equalTo(StatusMonitorConfiguration.DEFAULT_PASSWORD));
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
}