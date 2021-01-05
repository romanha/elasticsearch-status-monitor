package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

class StatusMonitorConfigurationTest {

    private StatusMonitorConfiguration sut;

    @BeforeEach
    void setUp() {
        sut = StatusMonitorConfigurations.random();
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