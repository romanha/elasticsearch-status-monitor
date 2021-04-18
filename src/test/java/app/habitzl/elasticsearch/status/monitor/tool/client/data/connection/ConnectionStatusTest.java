package app.habitzl.elasticsearch.status.monitor.tool.client.data.connection;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ConnectionStatusTest {

    @ParameterizedTest
    @MethodSource(value = "httpStatusToConnectionStatus")
    void fromHttpCode_httpStatusCode_returnConnectionStatus(final int httpStatusCode, final ConnectionStatus expected) {
        // When
        ConnectionStatus result = ConnectionStatus.fromHttpCode(httpStatusCode);

        // Then
        assertThat(result, equalTo(expected));
    }

    private static Stream<Arguments> httpStatusToConnectionStatus() {
        return Stream.of(
                Arguments.of(-1, ConnectionStatus.UNKNOWN),
                Arguments.of(0, ConnectionStatus.UNKNOWN),
                Arguments.of(200, ConnectionStatus.SUCCESS),
                Arguments.of(401, ConnectionStatus.UNAUTHORIZED),
                Arguments.of(404, ConnectionStatus.NOT_FOUND),
                Arguments.of(503, ConnectionStatus.SERVICE_UNAVAILABLE),
                Arguments.of(999, ConnectionStatus.UNKNOWN)
        );
    }
}