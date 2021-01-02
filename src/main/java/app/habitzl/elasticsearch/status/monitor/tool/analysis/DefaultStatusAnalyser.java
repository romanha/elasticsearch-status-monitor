package app.habitzl.elasticsearch.status.monitor.tool.analysis;

import app.habitzl.elasticsearch.status.monitor.tool.StatusAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.rest.RestStatus;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class DefaultStatusAnalyser implements StatusAnalyser {
	private static final Logger LOG = LogManager.getLogger(DefaultStatusAnalyser.class);

	private final ElasticsearchClient elasticsearchClient;

	@Inject
	public DefaultStatusAnalyser(final ElasticsearchClient elasticsearchClient) {
		this.elasticsearchClient = elasticsearchClient;
	}

	@Override
	public AnalysisReport createReport() {
		ConnectionInfo connectionInfo = elasticsearchClient.checkConnection();
		return startStatusMonitoringBasedOnConnection(connectionInfo);
	}

	/**
	 * Only starts status monitoring if the connection can be established and the REST status is OK.
	 */
	private AnalysisReport startStatusMonitoringBasedOnConnection(final ConnectionInfo connectionInfo) {
		AnalysisReport analysisReport;
		ConnectionStatus connectionStatus = connectionInfo.getConnectionStatus();
		switch (connectionStatus) {
			case SUCCESS:
				analysisReport = connectionInfo.getRestStatus()
											   .map(this::startStatusMonitoring)
											   .orElseGet(() -> abortStatusMonitoring(Problem.GENERAL_CONNECTION_FAILURE));
				break;
			case SSL_HANDSHAKE_FAILURE:
				analysisReport = abortStatusMonitoring(Problem.SSL_HANDSHAKE_FAILURE);
				break;
			case NOT_FOUND:
			default:
				analysisReport = abortStatusMonitoring(Problem.GENERAL_CONNECTION_FAILURE);
				break;
		}
		return analysisReport;
	}

	private AnalysisReport startStatusMonitoring(final RestStatus restStatus) {
		AnalysisReport analysisReport;
		switch (restStatus) {
			case OK:
				analysisReport = createAnalysisReport();
				break;
			case UNAUTHORIZED:
				analysisReport = abortStatusMonitoring(Problem.UNAUTHORIZED_CONNECTION_FAILURE);
				break;
			default:
				analysisReport = abortStatusMonitoringForOtherReason(restStatus);
				break;
		}

		return analysisReport;
	}

	private AnalysisReport createAnalysisReport() {
		Optional<ClusterInfo> clusterInfo = elasticsearchClient.getClusterInfo();
		List<NodeInfo> nodeInfos = elasticsearchClient.getNodeInfo();

		// TODO get more info, perform analysis to generate problems and warnings
		return AnalysisReport.create(
				List.of(),
				List.of(),
				clusterInfo.orElse(null),
				nodeInfos
		);
	}

	private AnalysisReport abortStatusMonitoring(final Problem problem) {
		LOG.warn("Encountered problem while gathering data: '{}'. Aborting status report generation.", problem);
		return AnalysisReport.aborted(List.of(problem));
	}

	private AnalysisReport abortStatusMonitoringForOtherReason(final RestStatus restStatus) {
		LOG.warn("Unexpected rest status retrieved: {}", restStatus);
		return abortStatusMonitoring(Problem.GENERAL_CONNECTION_FAILURE);
	}
}
