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
		String[] args = {
				"-" + DefaultConfigurationLoader.IP_OPTION, address,
				"-" + DefaultConfigurationLoader.PORT_OPTION, port,
				"-" + DefaultConfigurationLoader.UNSECURE_OPTION
		};

		// When
		sut.load(args);

		// Then
		assertThat(configuration.getIpAddress(), equalTo(address));
		assertThat(configuration.getPort(), equalTo(port));
		assertThat(configuration.isUsingHttps(), equalTo(false));
	}

	@Test
	void load_oneValidArgument_setConfigurationForOnlyThisArgument() {
		// Given
		String address = "1.2.3.4";
		String[] args = {
				"-" + DefaultConfigurationLoader.IP_OPTION, address
		};

		// When
		sut.load(args);

		// Then
		assertThat(configuration.getIpAddress(), equalTo(address));
		assertThat(configuration.getPort(), equalTo(StatusMonitorConfiguration.DEFAULT_PORT));
		assertThat(configuration.isUsingHttps(), equalTo(StatusMonitorConfiguration.DEFAULT_USING_HTTPS));
	}

	@Test
	void load_oneInvalidArgument_useDefaultConfiguration() {
		// Given
		String address = "1.2.3.4";
		String port = "9999";
		String[] args = {
				"-" + DefaultConfigurationLoader.IP_OPTION, address,
				"-unknownOption",
				"-" + DefaultConfigurationLoader.PORT_OPTION, port,
				"-" + DefaultConfigurationLoader.UNSECURE_OPTION
		};

		// When
		sut.load(args);

		// Then
		assertThat(configuration.getIpAddress(), equalTo(StatusMonitorConfiguration.DEFAULT_IP_ADDRESS));
		assertThat(configuration.getPort(), equalTo(StatusMonitorConfiguration.DEFAULT_PORT));
		assertThat(configuration.isUsingHttps(), equalTo(StatusMonitorConfiguration.DEFAULT_USING_HTTPS));
	}

	@Test
	void load_noArguments_useDefaultConfiguration() {
		// Given
		String[] args = {};

		// When
		sut.load(args);

		// Then
		assertThat(configuration.getIpAddress(), equalTo(StatusMonitorConfiguration.DEFAULT_IP_ADDRESS));
		assertThat(configuration.getPort(), equalTo(StatusMonitorConfiguration.DEFAULT_PORT));
		assertThat(configuration.isUsingHttps(), equalTo(StatusMonitorConfiguration.DEFAULT_USING_HTTPS));
	}
}