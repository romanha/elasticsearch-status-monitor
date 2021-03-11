package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

class AnalyserProviderTest {

    private AnalyserProvider sut;
    private EndpointAnalyser endpointAnalyser;
    private ClusterAnalyser clusterAnalyser;
    private ShardAnalyser shardAnalyser;

    @BeforeEach
    void setUp() {
        endpointAnalyser = mock(EndpointAnalyser.class);
        clusterAnalyser = mock(ClusterAnalyser.class);
        shardAnalyser = mock(ShardAnalyser.class);
        sut = new AnalyserProvider(endpointAnalyser, clusterAnalyser, shardAnalyser);
    }

    @Test
    void getEndpointAnalyser_always_returnEndpointAnalyser() {
        // Given
        // always

        // When
        EndpointAnalyser result = sut.getEndpointAnalyser();

        // Then
        assertThat(result, equalTo(endpointAnalyser));
    }

    @Test
    void getClusterAnalyser_always_returnClusterAnalyser() {
        // Given
        // always

        // When
        ClusterAnalyser result = sut.getClusterAnalyser();

        // Then
        assertThat(result, equalTo(clusterAnalyser));
    }

    @Test
    void getShardAnalyser_always_returnShardAnalyser() {
        // Given
        // always

        // When
        ShardAnalyser result = sut.getShardAnalyser();

        // Then
        assertThat(result, equalTo(shardAnalyser));
    }
}