package app.habitzl.elasticsearch.status.monitor.presentation;

import app.habitzl.elasticsearch.status.monitor.ClusterInfos;
import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import app.habitzl.elasticsearch.status.monitor.StatusMonitor;
import app.habitzl.elasticsearch.status.monitor.presentation.model.Problem;
import app.habitzl.elasticsearch.status.monitor.presentation.model.StatusReport;
import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

class DefaultStatusAggregatorTest {

	private DefaultStatusAggregator sut;
	private StatusMonitor statusMonitor;

	@BeforeEach
	void setUp() {
		statusMonitor = mock(StatusMonitor.class);
		sut = new DefaultStatusAggregator(statusMonitor);
	}

	@Test
	void createReport_clusterInfoNotAvailable_doNotAttemptMoreStatusRequests() {
		// Given
		// cluster info not available

		// When
		sut.createReport();

		// Then
		verify(statusMonitor).getClusterInfo();
		verifyNoMoreInteractions(statusMonitor);
	}

	@Test
	void createReport_clusterInfoNotAvailable_returnAbortedStatusReportWithConnectionFailureProblem() {
		// Given
		// cluster info not available

		// When
		StatusReport statusReport = sut.createReport();

		// Then
		StatusReport expectedStatusReport = StatusReport.aborted(List.of(Problem.CONNECTION_FAILURE));
		assertThat(statusReport, equalTo(expectedStatusReport));
	}

	@Test
	void createReport_clusterInfoAvailable_performStatusRequestsInOrder() {
		// Given
		givenAllStatusRequestsSucceed();

		// When
		sut.createReport();

		// Then
		InOrder inOrder = inOrder(statusMonitor);
		inOrder.verify(statusMonitor).getClusterInfo();
		inOrder.verify(statusMonitor).getNodeInfo();
	}

	@Test
	void createReport_clusterAvailable_returnStatusReportWithErrorConnectionStatus() {
		// Given
		StatusReport expectedStatusReport = givenAllStatusRequestsSucceed();

		// When
		StatusReport statusReport = sut.createReport();

		// Then
		assertThat(statusReport, equalTo(expectedStatusReport));
	}

	/**
	 * Returns the expected status report from all status monitor requests.
	 */
	private StatusReport givenAllStatusRequestsSucceed() {
		ClusterInfo clusterInfo = ClusterInfos.random();
		when(statusMonitor.getClusterInfo()).thenReturn(Optional.of(clusterInfo));
		List<NodeInfo> nodeInfos = List.of(NodeInfos.random());
		when(statusMonitor.getNodeInfo()).thenReturn(nodeInfos);
		return StatusReport.create(List.of(), List.of(), clusterInfo, nodeInfos);
	}
}