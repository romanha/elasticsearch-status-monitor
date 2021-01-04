package app.habitzl.elasticsearch.status.monitor.tool.analysis;

import app.habitzl.elasticsearch.status.monitor.ClusterInfos;
import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.GeneralConnectionProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.SSLHandshakeFailure;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.UnauthorizedConnectionProblem;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.rest.RestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class DefaultStatusAnalyserTest {

    private DefaultStatusAnalyser sut;
    private StatusMonitorConfiguration configuration;
    private ElasticsearchClient elasticsearchClient;

    @BeforeEach
    void setUp() {
        configuration = StatusMonitorConfigurations.random();
        elasticsearchClient = mock(ElasticsearchClient.class);
        sut = new DefaultStatusAnalyser(configuration, elasticsearchClient);
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
        AnalysisReport expectedAnalysisReport = AnalysisReport.aborted(configuration, List.of(SSLHandshakeFailure.create()));
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
        givenAllStatusRequestsSucceed();

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
        AnalysisReport expectedAnalysisReport = givenAllStatusRequestsSucceed();

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        assertThat(analysisReport, equalTo(expectedAnalysisReport));
    }

    /**
     * Returns the expected status report from all status monitor requests.
     */
    private AnalysisReport givenAllStatusRequestsSucceed() {
        when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.success(RestStatus.OK));
        ClusterInfo clusterInfo = ClusterInfos.random();
        when(elasticsearchClient.getClusterInfo()).thenReturn(Optional.of(clusterInfo));
        List<NodeInfo> nodeInfos = List.of(NodeInfos.random());
        when(elasticsearchClient.getNodeInfo()).thenReturn(nodeInfos);
        return AnalysisReport.create(configuration, List.of(), List.of(), clusterInfo, nodeInfos);
    }
}