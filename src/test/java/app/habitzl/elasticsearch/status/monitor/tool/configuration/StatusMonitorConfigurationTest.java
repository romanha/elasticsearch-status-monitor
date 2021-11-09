package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import app.habitzl.elasticsearch.status.monitor.Hosts;
import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class StatusMonitorConfigurationTest {

    private StatusMonitorConfiguration sut;

    @BeforeEach
    void setUp() {
        sut = StatusMonitorConfigurations.random();
    }

    @Test
    void getMainEndpoint_always_returnMainEndpoint() {
        // Given
        String expectedMainEndpoint = sut.getHost() + StatusMonitorConfiguration.HOST_PORT_SEPARATOR + sut.getPort();

        // When
        String result = sut.getMainEndpoint();

        // Then
        assertThat(result, equalTo(expectedMainEndpoint));
    }

    @Test
    void getAllEndpoints_noFallbackEndpoints_returnOnlyMainEndpoint() {
        // Given
        sut.setFallbackEndpoints(List.of());

        // When
        List<String> result = sut.getAllEndpoints();

        // Then
        String mainEndpoint = sut.getMainEndpoint();
        assertThat(result, contains(mainEndpoint));
    }

    @Test
    void getAllEndpoints_fallbackEndpoints_returnMainEndpointAndFallbackEndpoints() {
        // Given
        String fallbackEndpoint1 = Hosts.randomAddress();
        String fallbackEndpoint2 = Hosts.randomAddress();
        sut.setFallbackEndpoints(List.of(fallbackEndpoint1, fallbackEndpoint2));

        // When
        List<String> result = sut.getAllEndpoints();

        // Then
        String mainEndpoint = sut.getHost() + StatusMonitorConfiguration.HOST_PORT_SEPARATOR + sut.getPort();
        assertThat(result, contains(mainEndpoint, fallbackEndpoint1, fallbackEndpoint2));
    }

    @Test
    void toString_password_doesNotPrintPassword() {
        // Given
        String password = sut.getPassword();

        // When
        String result = sut.toString();

        // Then
        assertThat(result, not(containsString(password)));
    }
}