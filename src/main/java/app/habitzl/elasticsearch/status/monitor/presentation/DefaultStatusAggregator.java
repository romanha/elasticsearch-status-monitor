package app.habitzl.elasticsearch.status.monitor.presentation;

import app.habitzl.elasticsearch.status.monitor.StatusMonitor;
import app.habitzl.elasticsearch.status.monitor.presentation.model.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.presentation.model.StatusReport;
import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterHealthStatus;
import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

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

		ClusterInfo clusterInfo = statusMonitor.getClusterInfo();

		if (clusterInfo.getHealthStatus() == ClusterHealthStatus.UNKNOWN) {
			report = StatusReport.error(ConnectionStatus.UNABLE_TO_CONNECT);
		} else {
			List<NodeInfo> nodeInfos = statusMonitor.getNodeInfo();
			report = StatusReport.create(clusterInfo, nodeInfos);
		}

		closeStatusMonitorConnection();

		return report;
	}

	private void closeStatusMonitorConnection() {
		try {
			statusMonitor.closeConnection();
		} catch (IOException e) {
			LOG.warn("Could not safely close the connection to the ES cluster.", e);
		}
	}
}
