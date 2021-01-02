package app.habitzl.elasticsearch.status.monitor.tool.presentation.configuration;

import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class FreemarkerConfigurationProviderTest {

	private FreemarkerConfigurationProvider sut;

	@BeforeEach
	void setUp() {
		sut = new FreemarkerConfigurationProvider();
	}

	@Test
	void get_configurationNotYetExisting_returnsConfiguration() {
		// Given
		// configuration does not exist yet

		// When
		Configuration configuration = sut.get();

		// Then
		assertThat(configuration, notNullValue());
	}

	@Test
	void get_configurationAlreadyExists_returnExistingConfiguration() {
		// Given
		Configuration existingConfiguration = givenConfigurationAlreadyExists();

		// When
		Configuration configuration = sut.get();

		// Then
		assertThat(configuration, is(existingConfiguration));
	}

	private Configuration givenConfigurationAlreadyExists() {
		return sut.get();
	}
}