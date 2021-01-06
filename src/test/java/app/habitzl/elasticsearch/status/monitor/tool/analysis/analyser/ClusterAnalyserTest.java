package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.ClusterSettingsUtils;
import app.habitzl.elasticsearch.status.monitor.EndpointInfos;
import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.ClusterNotRedundantWarning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.SplitBrainPossibleWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ClusterAnalyserTest {

    private ClusterAnalyser sut;

    @BeforeEach
    void setUp() {
        sut = new ClusterAnalyser();
    }

    @Test
    void analyse_noNodes_returnsEmptyResult() {
        // Given
        List<NodeInfo> noNodes = List.of();

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(), noNodes);

        // Then
        AnalysisResult expected = AnalysisResult.empty();
        assertThat(result, equalTo(expected));
    }

    @Test
    void analyse_onlyOneNode_returnsClusterNotRedundantWarning() {
        // Given
        List<NodeInfo> singleNode = List.of(NodeInfos.randomMasterEligible());

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(), singleNode);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create();
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_multipleNodesOnDifferentEndpointsButOnlyOneMasterEligibleNode_returnsClusterNotRedundantWarning() {
        // Given
        List<NodeInfo> singleNode = List.of(
                NodeInfos.randomMasterEligible(),
                NodeInfos.randomNotMasterEligible(),
                NodeInfos.randomNotMasterEligible()
        );

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(), singleNode);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create();
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_twoMasterEligibleNodesOnSameEndpoint_returnsClusterNotRedundantWarning() {
        // Given
        EndpointInfo endpoint = EndpointInfos.random();
        List<NodeInfo> nodes = List.of(
                NodeInfos.randomMasterEligible(endpoint),
                NodeInfos.randomMasterEligible(endpoint),
                NodeInfos.randomNotMasterEligible()
        );

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(), nodes);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create();
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_multipleNodesOnDifferentEndpoints_doesNotReturnClusterNotRedundantWarning() {
        // Given
        EndpointInfo endpoint = EndpointInfos.random();
        List<NodeInfo> nodes = List.of(
                NodeInfos.randomMasterEligible(),
                NodeInfos.randomMasterEligible(endpoint),
                NodeInfos.randomMasterEligible(endpoint)
        );

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(), nodes);

        // Then
        ClusterNotRedundantWarning unexpectedWarning = ClusterNotRedundantWarning.create();
        assertThat(result.getWarnings(), not(hasItem(unexpectedWarning)));
    }

    @Test
    void analyse_oneMasterEligibleNodesAndOneRequiredMasterForElection_doesNotReturnSplitBrainPossibleWarning() {
        // Given
        ClusterSettings settings = new ClusterSettings(1);
        List<NodeInfo> nodes = List.of(NodeInfos.randomMasterEligible());

        // When
        AnalysisResult result = sut.analyse(settings, nodes);

        // Then
        SplitBrainPossibleWarning unexpectedWarning = SplitBrainPossibleWarning.create();
        assertThat(result.getWarnings(), not(hasItem(unexpectedWarning)));
    }

    @Test
    void analyse_twoMasterEligibleNodesButOnlyOneRequiredMasterForElection_returnsSplitBrainPossibleWarning() {
        // Given
        ClusterSettings settings = new ClusterSettings(1);
        List<NodeInfo> twoNodes = List.of(NodeInfos.randomMasterEligible(), NodeInfos.randomMasterEligible());

        // When
        AnalysisResult result = sut.analyse(settings, twoNodes);

        // Then
        SplitBrainPossibleWarning expectedWarning = SplitBrainPossibleWarning.create();
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }
}