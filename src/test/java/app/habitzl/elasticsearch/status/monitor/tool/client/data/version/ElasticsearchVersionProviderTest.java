/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.client.data.version;

import app.habitzl.elasticsearch.status.monitor.Randoms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ElasticsearchVersionProviderTest {

    private ElasticsearchVersionProvider sut;

    @BeforeEach
    void setUp() {
        sut = new ElasticsearchVersionProvider();
    }

    @Test
    void get_versionWasNeverUpdated_returnEmptyVersion() {
        // When
        ElasticsearchVersion result = sut.get();

        // Then
        ElasticsearchVersion expected = ElasticsearchVersion.defaultVersion();
        assertThat(result, equalTo(expected));
    }

    @Test
    void get_versionWasUpdated_returnUpdatedVersion() {
        // Given
        String version = Randoms.generateString("version-");
        sut.updateVersion(version);

        // When
        ElasticsearchVersion result = sut.get();

        // Then
        ElasticsearchVersion expected = new ElasticsearchVersion(version);
        assertThat(result, equalTo(expected));
    }
}