package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.cluster.ClusterAnalyserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AnalyserProviderTest {

    private AnalyserProvider sut;
    private EndpointAnalyser endpointAnalyser;
    private ClusterAnalyserProvider clusterAnalyserProvider;
    private ShardAnalyser shardAnalyser;

    @BeforeEach
    void setUp() {
        endpointAnalyser = mock(EndpointAnalyser.class);
        clusterAnalyserProvider = mock(ClusterAnalyserProvider.class);
        shardAnalyser = mock(ShardAnalyser.class);
        sut = new AnalyserProvider(endpointAnalyser, clusterAnalyserProvider, shardAnalyser);
    }

    @Test
    void getEndpointAnalyser_always_returnEndpointAnalyser() {
        // When
        EndpointAnalyser result = sut.getEndpointAnalyser();

        // Then
        assertThat(result, equalTo(endpointAnalyser));
    }

    @Test
    void getClusterAnalyser_always_getClusterAnalyserFromProvider() {
        // When
        sut.getClusterAnalyser();

        // Then
        verify(clusterAnalyserProvider).get();
    }

    @Test
    void getShardAnalyser_always_returnShardAnalyser() {
        // When
        ShardAnalyser result = sut.getShardAnalyser();

        // Then
        assertThat(result, equalTo(shardAnalyser));
    }
}