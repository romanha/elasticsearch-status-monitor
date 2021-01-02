package app.habitzl.elasticsearch.status.monitor.presentation;

import app.habitzl.elasticsearch.status.monitor.StatusMonitor;
import app.habitzl.elasticsearch.status.monitor.presentation.model.Problem;
import app.habitzl.elasticsearch.status.monitor.presentation.model.StatusReport;
import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		StatusReport report;

		Optional<ClusterInfo> clusterInfo = statusMonitor.getClusterInfo();

		if (clusterInfo.isEmpty()) {
			LOG.warn("Failed to retrieve cluster health state. Aborting status report generation.");
			report = StatusReport.aborted(List.of(Problem.CONNECTION_FAILURE));
		} else {
			List<NodeInfo> nodeInfos = statusMonitor.getNodeInfo();

			// TODO get more info, perform analysis to generate problems and warnings
			report = StatusReport.create(List.of(), List.of(), clusterInfo.get(), nodeInfos);
		}

		return report;
	}
}
