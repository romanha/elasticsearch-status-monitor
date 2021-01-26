package app.habitzl.elasticsearch.status.monitor.tool.client.data.connection;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class RestStatusTest {

    @ParameterizedTest
    @MethodSource(value = "httpStatusToRestStatus")
    void fromHttpCode_httpStatusCode_returnRestStatus(final int httpStatusCode, final RestStatus expected) {
        // When
        RestStatus result = RestStatus.fromHttpCode(httpStatusCode);

        // Then
        assertThat(result, equalTo(expected));
    }

    private static Stream<Arguments> httpStatusToRestStatus() {
        return Stream.of(
                Arguments.of(0, RestStatus.UNKNOWN_STATUS),
                Arguments.of(999, RestStatus.UNKNOWN_STATUS),
                Arguments.of(200, RestStatus.OK),
                Arguments.of(401, RestStatus.UNAUTHORIZED)
        );
    }
}