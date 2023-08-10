/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.cluster;

import app.habitzl.elasticsearch.status.monitor.ElasticsearchVersions;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.version.ElasticsearchVersion;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.version.ElasticsearchVersionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClusterAnalyserProviderTest {

    private ClusterAnalyserProvider sut;
    private ElasticsearchVersionProvider elasticsearchVersionProvider;
    private DefaultClusterAnalyser defaultClusterAnalyser;
    private Elasticsearch6ClusterAnalyser elasticsearch6ClusterAnalyser;

    @BeforeEach
    void setUp() {
        elasticsearchVersionProvider = mock(ElasticsearchVersionProvider.class);
        when(elasticsearchVersionProvider.get()).thenReturn(ElasticsearchVersion.defaultVersion());
        defaultClusterAnalyser = mock(DefaultClusterAnalyser.class);
        elasticsearch6ClusterAnalyser = mock(Elasticsearch6ClusterAnalyser.class);
        sut = new ClusterAnalyserProvider(elasticsearchVersionProvider, defaultClusterAnalyser, elasticsearch6ClusterAnalyser);
    }

    @Test
    void get_defaultElasticsearchVersion_returnDefaultClusterAnalyser() {
        // When
        ClusterAnalyser result = sut.get();

        // Then
        assertThat(result, instanceOf(DefaultClusterAnalyser.class));
        assertThat(result, equalTo(defaultClusterAnalyser));
    }

    @Test
    void get_elasticsearchVersion6_returnElasticsearch6ClusterAnalyser() {
        // Given
        when(elasticsearchVersionProvider.get()).thenReturn(ElasticsearchVersions.version6());

        // When
        ClusterAnalyser result = sut.get();

        // Then
        assertThat(result, instanceOf(Elasticsearch6ClusterAnalyser.class));
        assertThat(result, equalTo(elasticsearch6ClusterAnalyser));
    }

    @Test
    void get_elasticsearchVersion7_returnDefaultClusterAnalyser() {
        // Given
        when(elasticsearchVersionProvider.get()).thenReturn(ElasticsearchVersions.version7());

        // When
        ClusterAnalyser result = sut.get();

        // Then
        assertThat(result, instanceOf(DefaultClusterAnalyser.class));
        assertThat(result, equalTo(defaultClusterAnalyser));
    }
}