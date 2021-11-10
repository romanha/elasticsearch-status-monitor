package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import app.habitzl.elasticsearch.status.monitor.Hosts;
import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

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
    void getFallbackEndpoints_defaultConfig_returnEmpty() {
        // Given
        sut = StatusMonitorConfiguration.defaultConfig();

        // When
        List<String> result = sut.getFallbackEndpoints();

        // Then
        assertThat(result, empty());
    }

    @Test
    void getAllEndpoints_defaultConfig_returnOnlyMainEndpoint() {
        // Given
        sut = StatusMonitorConfiguration.defaultConfig();

        // When
        List<String> result = sut.getAllEndpoints();

        // Then
        String mainEndpoint = sut.getMainEndpoint();
        assertThat(result, contains(mainEndpoint));
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