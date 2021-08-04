package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class FallbackEndpointRestClientProviderTest {

    private FallbackEndpointRestClientProvider sut;
    private FallbackRestClientFactory factory;

    @BeforeEach
    void setUp() {
        factory = mock(FallbackRestClientFactory.class);
        sut = new FallbackEndpointRestClientProvider(factory);
    }

    @Test
    void get_clientsNotYetExisting_createNewClient() {
        // Given
        // clients do not exist yet

        // When
        sut.get();

        // Then
        verify(factory).create();
    }

    @Test
    void get_clientsAlreadyExist_doNotCreateNewClients() {
        // Given
        givenClientsAlreadyExist();

        // When
        sut.get();

        // Then
        verifyNoMoreInteractions(factory);
    }

    private void givenClientsAlreadyExist() {
        when(factory.create()).thenReturn(List.of(mock(RestClient.class)));
        sut.get();
        verify(factory).create();
    }
}