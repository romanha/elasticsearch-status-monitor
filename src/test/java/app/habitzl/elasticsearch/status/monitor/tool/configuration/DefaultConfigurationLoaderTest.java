package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DefaultConfigurationLoaderTest {

    private DefaultConfigurationLoader sut;
    private StatusMonitorConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new StatusMonitorConfiguration();
        sut = new DefaultConfigurationLoader(configuration);
    }

    @Test
    void load_validArguments_setConfiguration() {
        // Given
        String address = "1.2.3.4";
        String port = "9999";
        String username = "username";
        String password = "password";
        String[] args = {
                "-" + DefaultConfigurationLoader.HOST_OPTION, address,
                "-" + DefaultConfigurationLoader.PORT_OPTION, port,
                "-" + DefaultConfigurationLoader.SECURITY_OPTION, "false",
                "-" + DefaultConfigurationLoader.USER_OPTION, username,
                "-" + DefaultConfigurationLoader.PASSWORD_OPTION, password
        };

        // When
        sut.load(args);

        // Then
        assertThat(configuration.getHost(), equalTo(address));
        assertThat(configuration.getPort(), equalTo(port));
        assertThat(configuration.isUsingHttps(), equalTo(false));
        assertThat(configuration.getUsername(), equalTo(username));
        assertThat(configuration.getPassword(), equalTo(password));
    }

    @Test
    void load_validLongArguments_setConfiguration() {
        // Given
        String address = "1.2.3.4";
        String port = "9999";
        String username = "username";
        String password = "password";
        String[] args = {
                "--" + DefaultConfigurationLoader.HOST_OPTION_LONG, address,
                "--" + DefaultConfigurationLoader.PORT_OPTION_LONG, port,
                "--" + DefaultConfigurationLoader.SECURITY_OPTION_LONG, "true",
                "--" + DefaultConfigurationLoader.USER_OPTION_LONG, username,
                "--" + DefaultConfigurationLoader.PASSWORD_OPTION_LONG, password
        };

        // When
        sut.load(args);

        // Then
        assertThat(configuration.getHost(), equalTo(address));
        assertThat(configuration.getPort(), equalTo(port));
        assertThat(configuration.isUsingHttps(), equalTo(true));
        assertThat(configuration.getUsername(), equalTo(username));
        assertThat(configuration.getPassword(), equalTo(password));
    }

    @Test
    void load_oneValidArgument_setConfigurationForOnlyThisArgument() {
        // Given
        String address = "1.2.3.4";
        String[] args = {
                "-" + DefaultConfigurationLoader.HOST_OPTION, address
        };

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
    void load_oneInvalidArgument_useDefaultConfiguration() {
        // Given
        String address = "1.2.3.4";
        String port = "9999";
        String[] args = {
                "-" + DefaultConfigurationLoader.HOST_OPTION, address,
                "--unknown",
                "-" + DefaultConfigurationLoader.PORT_OPTION, port
        };

        // When
        sut.load(args);

        // Then
        assertThatConfigurationIsDefault();
    }

    @Test
    void load_noArguments_useDefaultConfiguration() {
        // Given
        String[] args = {};

        // When
        sut.load(args);

        // Then
        assertThatConfigurationIsDefault();
    }

    @Test
    void load_securityOptionWithUnknownValue_enableSecurity() {
        // Given
        String[] args = {
                "-" + DefaultConfigurationLoader.SECURITY_OPTION, "unknown"
        };

        // When
        sut.load(args);

        // Then
        assertThat(configuration.isUsingHttps(), equalTo(true));
    }

    private void assertThatConfigurationIsDefault() {
        assertThat(configuration.getHost(), equalTo(StatusMonitorConfiguration.DEFAULT_HOST));
        assertThat(configuration.getPort(), equalTo(StatusMonitorConfiguration.DEFAULT_PORT));
        assertThat(configuration.isUsingHttps(), equalTo(StatusMonitorConfiguration.DEFAULT_USING_HTTPS));
        assertThat(configuration.getUsername(), equalTo(StatusMonitorConfiguration.DEFAULT_USERNAME));
        assertThat(configuration.getPassword(), equalTo(StatusMonitorConfiguration.DEFAULT_PASSWORD));
    }
}