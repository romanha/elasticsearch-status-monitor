package app.habitzl.elasticsearch.status.monitor.presentation;

import app.habitzl.elasticsearch.status.monitor.StatusMonitor;
import app.habitzl.elasticsearch.status.monitor.presentation.model.Problem;
import app.habitzl.elasticsearch.status.monitor.presentation.model.StatusReport;
import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.rest.RestStatus;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class DefaultStatusAggregator implements StatusAggregator {
	private static final Logger LOG = LogManager.getLogger(DefaultStatusAggregator.class);

	private final StatusMonitor statusMonitor;

	@Inject
	public DefaultStatusAggregator(final StatusMonitor statusMonitor) {
		this.statusMonitor = statusMonitor;
	}

	@Override
	public StatusReport createReport() {
		Optional<RestStatus> restStatus = statusMonitor.checkConnection();
		return restStatus.map(this::startStatusMonitoring)
						 .orElseGet(() -> abortStatusMonitoring(Problem.GENERAL_CONNECTION_FAILURE));
	}

	private StatusReport startStatusMonitoring(final RestStatus restStatus) {
		StatusReport statusReport;
		switch (restStatus) {
			case OK:
				statusReport = generateStatusReport();
				break;
			case UNAUTHORIZED:
				statusReport = abortStatusMonitoring(Problem.UNAUTHORIZED_CONNECTION_FAILURE);
				break;
			default:
				statusReport = abortStatusMonitoringForOtherReason(restStatus);
				break;
		}

		return statusReport;
	}

	private StatusReport generateStatusReport() {
		Optional<ClusterInfo> clusterInfo = statusMonitor.getClusterInfo();
		List<NodeInfo> nodeInfos = statusMonitor.getNodeInfo();

		// TODO get more info, perform analysis to generate problems and warnings
		return StatusReport.create(
				List.of(),
				List.of(),
				clusterInfo.orElse(null),
				nodeInfos
		);
	}

	private StatusReport abortStatusMonitoring(final Problem problem) {
		LOG.warn("Encountered problem while gathering data: '{}'. Aborting status report generation.", problem);
		return StatusReport.aborted(List.of(problem));
	}

	private StatusReport abortStatusMonitoringForOtherReason(final RestStatus restStatus) {
		LOG.warn("Unexpected rest status retrieved: {}", restStatus);
		return abortStatusMonitoring(Problem.GENERAL_CONNECTION_FAILURE);
	}
}
