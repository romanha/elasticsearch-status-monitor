package app.habitzl.elasticsearch.status.monitor.tool.analysis;

import app.habitzl.elasticsearch.status.monitor.ClusterInfos;
import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.ClusterAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.EndpointAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.GeneralConnectionProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.SSLHandshakeProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.UnauthorizedConnectionProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.ClusterNotRedundantWarning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.HighRamUsageWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import org.elasticsearch.rest.RestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

class DefaultStatusAnalyserTest {

    private DefaultStatusAnalyser sut;
    private StatusMonitorConfiguration configuration;
    private ElasticsearchClient elasticsearchClient;
    private EndpointAnalyser endpointAnalyser;
    private ClusterAnalyser clusterAnalyser;

    @BeforeEach
    void setUp() {
        configuration = StatusMonitorConfigurations.random();
        elasticsearchClient = mock(ElasticsearchClient.class);
        mockEndpointAnalyser();
        mockClusterAnalyser();
        sut = new DefaultStatusAnalyser(configuration, elasticsearchClient, endpointAnalyser, clusterAnalyser);
    }

    @Test
    void createReport_connectionStatusNotSuccess_doNotAttemptMoreStatusRequests() {
        // Given
        when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.error(ConnectionStatus.NOT_FOUND, ""));

        // When
        sut.createReport();

        // Then
        verify(elasticsearchClient).checkConnection();
        verifyNoMoreInteractions(elasticsearchClient);
    }

    @Test
    void createReport_restStatusUnauthorized_doNotAttemptMoreStatusRequests() {
        // Given
        when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.success(RestStatus.UNAUTHORIZED));

        // When
        sut.createReport();

        // Then
        verify(elasticsearchClient).checkConnection();
        verifyNoMoreInteractions(elasticsearchClient);
    }

    @Test
    void createReport_connectionStatusNotFound_returnAbortedAnalysisReportWithGeneralConnectionProblem() {
        // Given
        String connectionErrorInformation = "some connection error";
        when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.error(ConnectionStatus.NOT_FOUND, connectionErrorInformation));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        AnalysisReport expectedAnalysisReport = AnalysisReport.aborted(configuration, List.of(GeneralConnectionProblem.create(connectionErrorInformation)));
        assertThat(analysisReport, equalTo(expectedAnalysisReport));
    }

    @Test
    void createReport_connectionStatusSSLHandshakeFailure_returnAbortedAnalysisReportWithSSLHandshakeProblem() {
        // Given
        when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.error(ConnectionStatus.SSL_HANDSHAKE_FAILURE, ""));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        AnalysisReport expectedAnalysisReport = AnalysisReport.aborted(configuration, List.of(SSLHandshakeProblem.create()));
        assertThat(analysisReport, equalTo(expectedAnalysisReport));
    }

    @Test
    void createReport_restStatusUnauthorized_returnAbortedAnalysisReportWithUnauthorizedConnectionProblem() {
        // Given
        when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.success(RestStatus.UNAUTHORIZED));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        AnalysisReport expectedAnalysisReport = AnalysisReport.aborted(configuration, List.of(UnauthorizedConnectionProblem.create()));
        assertThat(analysisReport, equalTo(expectedAnalysisReport));
    }

    @Test
    void createReport_allStatusRequestsSucceed_performStatusRequestsInOrder() {
        // Given
        givenAllRequestsSucceed();

        // When
        sut.createReport();

        // Then
        InOrder inOrder = inOrder(elasticsearchClient);
        inOrder.verify(elasticsearchClient).checkConnection();
        inOrder.verify(elasticsearchClient).getClusterInfo();
        inOrder.verify(elasticsearchClient).getNodeInfo();
    }

    @Test
    void createReport_allStatusRequestsSucceed_returnExpectedAnalysisReport() {
        // Given
        AnalysisReport expectedAnalysisReport = givenAllRequestsSucceed();

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        assertThat(analysisReport, equalTo(expectedAnalysisReport));
    }

    @Test
    void createReport_endpointAnalyserFindsWarning_returnExpectedAnalysisReport() {
        // Given
        Warning warning = givenEndpointAnalyserFindsWarning();
        AnalysisReport expectedAnalysisReport = givenAllRequestsSucceedWithWarnings(List.of(warning));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        assertThat(analysisReport, equalTo(expectedAnalysisReport));
    }

    @Test
    void createReport_clusterAnalyserFindsWarning_returnExpectedAnalysisReport() {
        // Given
        Warning warning = givenClusterAnalyserFindsWarning();
        AnalysisReport expectedAnalysisReport = givenAllRequestsSucceedWithWarnings(List.of(warning));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        assertThat(analysisReport, equalTo(expectedAnalysisReport));
    }

    @Test
    void createReport_everyAnalyserFindsWarning_returnExpectedAnalysisReport() {
        // Given
        Warning warning1 = givenEndpointAnalyserFindsWarning();
        Warning warning2 = givenClusterAnalyserFindsWarning();
        AnalysisReport expectedAnalysisReport = givenAllRequestsSucceedWithWarnings(List.of(warning1, warning2));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        assertThat(analysisReport, equalTo(expectedAnalysisReport));
    }

    private void mockEndpointAnalyser() {
        endpointAnalyser = mock(EndpointAnalyser.class);
        when(endpointAnalyser.analyse(anyList())).thenReturn(AnalysisResult.empty());
    }

    private void mockClusterAnalyser() {
        clusterAnalyser = mock(ClusterAnalyser.class);
        when(clusterAnalyser.analyse(any(ClusterSettings.class), anyList())).thenReturn(AnalysisResult.empty());
    }

    /**
     * Returns the expected status report from all status monitor requests.
     */
    private AnalysisReport givenAllRequestsSucceed() {
        ClusterInfo clusterInfo = ClusterInfos.random();
        List<NodeInfo> nodeInfos = List.of(NodeInfos.randomNode());
        givenAllRequestsSucceed(clusterInfo, nodeInfos);
        return AnalysisReport.create(configuration, List.of(), List.of(), clusterInfo, nodeInfos);
    }

    /**
     * Returns the expected status report from all status monitor requests.
     */
    private AnalysisReport givenAllRequestsSucceedWithWarnings(final List<Warning> warnings) {
        ClusterInfo clusterInfo = ClusterInfos.random();
        List<NodeInfo> nodeInfos = List.of(NodeInfos.randomNode());
        givenAllRequestsSucceed(clusterInfo, nodeInfos);
        return AnalysisReport.create(configuration, List.of(), warnings, clusterInfo, nodeInfos);
    }

    private void givenAllRequestsSucceed(final ClusterInfo clusterInfo, final List<NodeInfo> nodeInfos) {
        when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.success(RestStatus.OK));
        when(elasticsearchClient.getClusterInfo()).thenReturn(Optional.of(clusterInfo));
        when(elasticsearchClient.getNodeInfo()).thenReturn(nodeInfos);
    }

    private Warning givenEndpointAnalyserFindsWarning() {
        HighRamUsageWarning warning = HighRamUsageWarning.create(Set.of("endpoint"));
        when(endpointAnalyser.analyse(anyList()))
                .thenReturn(AnalysisResult.create(List.of(), List.of(warning)));
        return warning;
    }

    private Warning givenClusterAnalyserFindsWarning() {
        ClusterNotRedundantWarning warning = ClusterNotRedundantWarning.create(true, true, true);
        when(clusterAnalyser.analyse(any(ClusterSettings.class), anyList()))
                .thenReturn(AnalysisResult.create(List.of(), List.of(warning)));
        return warning;
    }
}