package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.EndpointInfos;
import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.ClusterNotRedundantWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ClusterAnalyserTest {

    private ClusterAnalyser sut;

    @BeforeEach
    void setUp() {
        sut = new ClusterAnalyser();
    }

    @Test
    void analyse_emptyList_returnsEmptyResult() {
        // Given
        List<NodeInfo> noNodes = List.of();

        // When
        AnalysisResult result = sut.analyse(noNodes);

        // Then
        AnalysisResult expected = AnalysisResult.empty();
        assertThat(result, equalTo(expected));
    }

    @Test
    void analyse_onlyOneNode_returnsClusterNotRedundantWarning() {
        // Given
        List<NodeInfo> singleNode = List.of(NodeInfos.random());

        // When
        AnalysisResult result = sut.analyse(singleNode);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create();
        AnalysisResult expected = AnalysisResult.create(List.of(), List.of(expectedWarning));
        assertThat(result, equalTo(expected));
    }

    @Test
    void analyse_twoNodesOnSameEndpoint_returnsClusterNotRedundantWarning() {
        // Given
        EndpointInfo endpoint = EndpointInfos.random();
        List<NodeInfo> nodes = List.of(NodeInfos.random(endpoint), NodeInfos.random(endpoint));

        // When
        AnalysisResult result = sut.analyse(nodes);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create();
        AnalysisResult expected = AnalysisResult.create(List.of(), List.of(expectedWarning));
        assertThat(result, equalTo(expected));
    }

    @Test
    void analyse_multipleNodesOnDifferentEndpoints_returnsEmptyResult() {
        // Given
        EndpointInfo endpoint = EndpointInfos.random();
        List<NodeInfo> nodes = List.of(NodeInfos.random(), NodeInfos.random(endpoint), NodeInfos.random(endpoint));

        // When
        AnalysisResult result = sut.analyse(nodes);

        // Then
        AnalysisResult expected = AnalysisResult.empty();
        assertThat(result, equalTo(expected));
    }
}