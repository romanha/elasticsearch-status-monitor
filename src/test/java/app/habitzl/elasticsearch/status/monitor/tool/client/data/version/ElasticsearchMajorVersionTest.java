/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.client.data.version;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ElasticsearchMajorVersionTest {

    @ParameterizedTest
    @MethodSource(value = "stringVersionToMajorVersionMapping")
    void fromVersionString_always_returnExpectedMajorVersion(final String versionString, final ElasticsearchMajorVersion expected) {
        // When
        ElasticsearchMajorVersion result = ElasticsearchMajorVersion.fromVersionString(versionString);

        // Then
        assertThat(result, equalTo(expected));
    }

    static Stream<Arguments> stringVersionToMajorVersionMapping() {
        return Stream.of(
                Arguments.of(null, ElasticsearchMajorVersion.UNKNOWN),
                Arguments.of("", ElasticsearchMajorVersion.UNKNOWN),
                Arguments.of(".8", ElasticsearchMajorVersion.UNKNOWN),
                Arguments.of(".8.8", ElasticsearchMajorVersion.UNKNOWN),
                Arguments.of("unknown", ElasticsearchMajorVersion.UNKNOWN),
                Arguments.of("5", ElasticsearchMajorVersion.UNKNOWN),
                Arguments.of("9", ElasticsearchMajorVersion.UNKNOWN),
                Arguments.of("77", ElasticsearchMajorVersion.UNKNOWN),
                Arguments.of("6", ElasticsearchMajorVersion.SIX),
                Arguments.of("6.8", ElasticsearchMajorVersion.SIX),
                Arguments.of("6.8.8", ElasticsearchMajorVersion.SIX),
                Arguments.of("7", ElasticsearchMajorVersion.SEVEN),
                Arguments.of("7.17", ElasticsearchMajorVersion.SEVEN),
                Arguments.of("7.17.8", ElasticsearchMajorVersion.SEVEN),
                Arguments.of("8", ElasticsearchMajorVersion.EIGHT),
                Arguments.of("8.1", ElasticsearchMajorVersion.EIGHT),
                Arguments.of("8.1.0", ElasticsearchMajorVersion.EIGHT)
        );
    }
}