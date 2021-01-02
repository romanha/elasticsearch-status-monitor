package app.habitzl.elasticsearch.status.monitor.tool.analysis;

import app.habitzl.elasticsearch.status.monitor.ClusterInfos;
import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import org.elasticsearch.rest.RestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

class DefaultStatusAnalyserTest {

	private DefaultStatusAnalyser sut;
	private ElasticsearchClient elasticsearchClient;

	@BeforeEach
	void setUp() {
		elasticsearchClient = mock(ElasticsearchClient.class);
		sut = new DefaultStatusAnalyser(elasticsearchClient);
	}

	@Test
	void createReport_connectionStatusNotSuccess_doNotAttemptMoreStatusRequests() {
		// Given
		when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.error(ConnectionStatus.NOT_FOUND));

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
		when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.error(ConnectionStatus.NOT_FOUND));

		// When
		AnalysisReport analysisReport = sut.createReport();

		// Then
		AnalysisReport expectedAnalysisReport = AnalysisReport.aborted(List.of(Problem.GENERAL_CONNECTION_FAILURE));
		assertThat(analysisReport, equalTo(expectedAnalysisReport));
	}

	@Test
	void createReport_connectionStatusSSLHandshakeFailure_returnAbortedAnalysisReportWithSSLHandshakeProblem() {
		// Given
		when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.error(ConnectionStatus.SSL_HANDSHAKE_FAILURE));

		// When
		AnalysisReport analysisReport = sut.createReport();

		// Then
		AnalysisReport expectedAnalysisReport = AnalysisReport.aborted(List.of(Problem.SSL_HANDSHAKE_FAILURE));
		assertThat(analysisReport, equalTo(expectedAnalysisReport));
	}

	@Test
	void createReport_restStatusUnauthorized_returnAbortedAnalysisReportWithUnauthorizedConnectionProblem() {
		// Given
		when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.success(RestStatus.UNAUTHORIZED));

		// When
		AnalysisReport analysisReport = sut.createReport();

		// Then
		AnalysisReport expectedAnalysisReport = AnalysisReport.aborted(List.of(Problem.UNAUTHORIZED_CONNECTION_FAILURE));
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
		return AnalysisReport.create(List.of(), List.of(), clusterInfo, nodeInfos);
	}
}