package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.cluster;

import app.habitzl.elasticsearch.status.monitor.ClusterSettingsUtils;
import app.habitzl.elasticsearch.status.monitor.EndpointInfos;
import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.ClusterNotRedundantWarning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.SplitBrainPossibleWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

class Elasticsearch6ClusterAnalyserTest {

    private Elasticsearch6ClusterAnalyser sut;

    @BeforeEach
    void setUp() {
        sut = new Elasticsearch6ClusterAnalyser();
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
        List<NodeInfo> singleNode = List.of(NodeInfos.randomMasterEligibleDataNode());

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(1), singleNode);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create(true, true, true);
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_multipleDataNodesOnDifferentEndpointsButOnlyOneMasterEligibleNode_returnsClusterNotRedundantWarning() {
        // Given
        List<NodeInfo> nodes = List.of(
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomNotMasterEligibleDataNode(),
                NodeInfos.randomNotMasterEligibleDataNode()
        );

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(2), nodes);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create(true, false, false);
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_multipleMasterEligibleNodesOnDifferentEndpointsButOnlyOneDataNode_returnsClusterNotRedundantWarning() {
        // Given
        List<NodeInfo> nodes = List.of(
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleNode(EndpointInfos.random(), false),
                NodeInfos.randomMasterEligibleNode(EndpointInfos.random(), false)
        );

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(2), nodes);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create(false, true, false);
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_requiredMasterNodesForElectionIsSameAsMasterEligibleNodes_returnsClusterNotRedundantWarning() {
        // Given
        ClusterSettings settings = ClusterSettingsUtils.random(3);
        List<NodeInfo> nodes = List.of(
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode()
        );

        // When
        AnalysisResult result = sut.analyse(settings, nodes);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create(false, false, true);
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_twoMasterEligibleNodesOnSameEndpoint_returnsClusterNotRedundantWarning() {
        // Given
        EndpointInfo endpoint = EndpointInfos.random();
        List<NodeInfo> nodes = List.of(
                NodeInfos.randomMasterEligibleDataNode(endpoint),
                NodeInfos.randomMasterEligibleDataNode(endpoint),
                NodeInfos.randomNotMasterEligibleDataNode()
        );

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(2), nodes);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create(true, false, true);
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_twoDataNodesOnSameEndpoint_returnsClusterNotRedundantWarning() {
        // Given
        EndpointInfo endpoint = EndpointInfos.random();
        List<NodeInfo> nodes = List.of(
                NodeInfos.randomMasterEligibleDataNode(endpoint),
                NodeInfos.randomMasterEligibleDataNode(endpoint),
                NodeInfos.randomMasterEligibleNode(EndpointInfos.random(), false)
        );

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(2), nodes);

        // Then
        ClusterNotRedundantWarning expectedWarning = ClusterNotRedundantWarning.create(false, true, false);
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_multipleMasterEligibleDataNodesOnDifferentEndpoints_doesNotReturnClusterNotRedundantWarning() {
        // Given
        EndpointInfo endpoint = EndpointInfos.random();
        List<NodeInfo> nodes = List.of(
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode(endpoint),
                NodeInfos.randomMasterEligibleDataNode(endpoint)
        );

        // When
        AnalysisResult result = sut.analyse(ClusterSettingsUtils.random(2), nodes);

        // Then
        assertThat(result.getWarnings(), not(hasItem(any(ClusterNotRedundantWarning.class))));
    }

    @Test
    void analyse_oneMasterEligibleNodesAndOneRequiredMasterNodeForElection_doesNotReturnSplitBrainPossibleWarning() {
        // Given
        ClusterSettings settings = ClusterSettingsUtils.random(1);
        List<NodeInfo> nodes = List.of(NodeInfos.randomMasterEligibleDataNode());

        // When
        AnalysisResult result = sut.analyse(settings, nodes);

        // Then
        assertThat(result.getWarnings(), not(hasItem(any(SplitBrainPossibleWarning.class))));
    }

    @Test
    void analyse_twoMasterEligibleNodesButOnlyOneRequiredMasterNodeForElection_returnsSplitBrainPossibleWarning() {
        // Given
        ClusterSettings settings = ClusterSettingsUtils.random(1);
        List<NodeInfo> twoNodes = List.of(
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode()
        );

        // When
        AnalysisResult result = sut.analyse(settings, twoNodes);

        // Then
        SplitBrainPossibleWarning expectedWarning = SplitBrainPossibleWarning.create(1, 2, 2);
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_fiveMasterEligibleNodesButOnlyTwoRequiredMasterNodeForElection_returnsSplitBrainPossibleWarning() {
        // Given
        ClusterSettings settings = ClusterSettingsUtils.random(2);
        List<NodeInfo> twoNodes = List.of(
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode()
        );

        // When
        AnalysisResult result = sut.analyse(settings, twoNodes);

        // Then
        SplitBrainPossibleWarning expectedWarning = SplitBrainPossibleWarning.create(2, 3, 5);
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }

    @Test
    void analyse_fiveMasterEligibleNodesAndThreeRequiredMasterNodeForElection_doesNotReturnSplitBrainPossibleWarning() {
        // Given
        ClusterSettings settings = ClusterSettingsUtils.random(3);
        List<NodeInfo> twoNodes = List.of(
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode(),
                NodeInfos.randomMasterEligibleDataNode()
        );

        // When
        AnalysisResult result = sut.analyse(settings, twoNodes);

        // Then
        assertThat(result.getWarnings(), not(hasItem(any(SplitBrainPossibleWarning.class))));
    }
}