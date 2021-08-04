package app.habitzl.elasticsearch.status.monitor.tool.analysis;

import app.habitzl.elasticsearch.status.monitor.tool.StatusAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.AnalyserProvider;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.ClusterNotFullyOperationalProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.GeneralConnectionProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.SSLHandshakeProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.UnauthorizedConnectionProblem;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import app.habitzl.elasticsearch.status.monitor.util.TimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.Clock;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultStatusAnalyser implements StatusAnalyser {
    private static final Logger LOG = LogManager.getLogger(DefaultStatusAnalyser.class);

    private final Clock clock;
    private final TimeFormatter timeFormatter;
    private final StatusMonitorConfiguration configuration;
    private final ElasticsearchClient elasticsearchClient;
    private final AnalyserProvider analyserProvider;

    @Inject
    public DefaultStatusAnalyser(
            final Clock clock,
            final TimeFormatter timeFormatter,
            final StatusMonitorConfiguration configuration,
            final ElasticsearchClient elasticsearchClient,
            final AnalyserProvider analyserProvider) {
        this.clock = clock;
        this.timeFormatter = timeFormatter;
        this.configuration = configuration;
        this.elasticsearchClient = elasticsearchClient;
        this.analyserProvider = analyserProvider;
    }

    @Override
    public AnalysisReport createReport() {
        ConnectionInfo connectionInfo = elasticsearchClient.checkConnection();
        return startStatusMonitoringBasedOnConnection(connectionInfo);
    }

    /**
     * Only starts status monitoring if the connection can be established.
     */
    private AnalysisReport startStatusMonitoringBasedOnConnection(final ConnectionInfo connectionInfo) {
        AnalysisReport analysisReport;
        ConnectionStatus connectionStatus = connectionInfo.getConnectionStatus();
        switch (connectionStatus) {
            case SUCCESS:
                analysisReport = createAnalysisReport();
                break;
            case SSL_HANDSHAKE_FAILURE:
                analysisReport = abortStatusMonitoring(SSLHandshakeProblem.create());
                break;
            case UNAUTHORIZED:
                analysisReport = abortStatusMonitoring(UnauthorizedConnectionProblem.create());
                break;
            case SERVICE_UNAVAILABLE:
                analysisReport = abortStatusMonitoring(ClusterNotFullyOperationalProblem.create());
                break;
            case NOT_FOUND:
            case UNKNOWN:
            default:
                analysisReport = abortStatusMonitoring(GeneralConnectionProblem.create(connectionInfo.getConnectionErrorInformation().orElse("")));
                break;
        }

        return analysisReport;
    }

    private AnalysisReport createAnalysisReport() {
        // TODO pass ConnectionInfo as parameter to this method, it can contain info about not reachable endpoints
        //      create a warning if some endpoints were not reached

        ClusterSettings clusterSettings = elasticsearchClient.getClusterSettings().orElse(ClusterSettings.createDefault());
        Optional<ClusterInfo> clusterInfo = elasticsearchClient.getClusterInfo();
        List<NodeInfo> nodeInfos = elasticsearchClient.getNodeInfo();
        Optional<UnassignedShardInfo> unassignedShardInfo =
                clusterInfo.filter(info -> info.getNumberOfUnassignedShards() > 0)
                        .flatMap(info -> elasticsearchClient.getUnassignedShardInfo());

        AnalysisResult endpointAnalysisResult = analyserProvider.getEndpointAnalyser()
                .analyse(nodeInfos.stream().map(NodeInfo::getEndpointInfo).collect(Collectors.toList()));
        AnalysisResult clusterAnalysisResult = analyserProvider.getClusterAnalyser()
                .analyse(clusterSettings, nodeInfos);
        AnalysisResult shardAnalysisResult = analyserProvider.getShardAnalyser()
                .analyse(unassignedShardInfo.orElse(null));

        AnalysisResult analysisResult = combineAnalysisResults(
                endpointAnalysisResult,
                clusterAnalysisResult,
                shardAnalysisResult
        );

        return AnalysisReport.finished(
                getTimestamp(),
                configuration,
                analysisResult.getProblems(),
                analysisResult.getWarnings(),
                clusterInfo.orElse(null),
                nodeInfos
        );
    }

    private AnalysisResult combineAnalysisResults(final AnalysisResult... analysisResults) {
        List<Problem> allProblems = Arrays.stream(analysisResults)
                .map(AnalysisResult::getProblems)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        List<Warning> allWarnings = Arrays.stream(analysisResults)
                .map(AnalysisResult::getWarnings)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return AnalysisResult.create(allProblems, allWarnings);
    }

    private AnalysisReport abortStatusMonitoring(final Problem problem) {
        LOG.warn("Encountered problem while gathering data: '{}'. Aborting status report generation.", problem);
        return AnalysisReport.aborted(getTimestamp(), configuration, List.of(problem));
    }

    private String getTimestamp() {
        return timeFormatter.format(clock.instant());
    }
}
