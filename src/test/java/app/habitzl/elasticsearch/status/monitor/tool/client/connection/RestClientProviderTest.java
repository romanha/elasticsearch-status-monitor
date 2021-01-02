package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RestClientProviderTest {

	private RestClientProvider sut;
	private RestClientFactory factory;

	@BeforeEach
	void setUp() {
		factory = mock(RestClientFactory.class);
		sut = new RestClientProvider(factory);
	}

	@Test
	void get_clientNotYetExisting_createNewClient() {
		// Given
		// client does not exist yet

		// When
		sut.get();

		// Then
		verify(factory).create();
	}

	@Test
	void get_clientAlreadyExists_doNotCreateNewClient() {
		// Given
		givenClientAlreadyExists();

		// When
		sut.get();

		// Then
		verifyNoMoreInteractions(factory);
	}

	private void givenClientAlreadyExists() {
		when(factory.create()).thenReturn(mock(RestHighLevelClient.class));
		sut.get();
		verify(factory).create();
	}
}