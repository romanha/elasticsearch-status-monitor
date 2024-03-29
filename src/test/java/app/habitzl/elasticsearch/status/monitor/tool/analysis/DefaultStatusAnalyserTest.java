package app.habitzl.elasticsearch.status.monitor.tool.analysis;

import app.habitzl.elasticsearch.status.monitor.Clocks;
import app.habitzl.elasticsearch.status.monitor.ClusterInfos;
import app.habitzl.elasticsearch.status.monitor.ClusterNodeInfos;
import app.habitzl.elasticsearch.status.monitor.Randoms;
import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import app.habitzl.elasticsearch.status.monitor.UnassignedShardInfos;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.AnalyserProvider;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.ClusterAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.ClusterAnalyserProvider;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.EndpointAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.ShardAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.ClusterNotFullyOperationalProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.GeneralConnectionProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.SSLHandshakeProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.UnauthorizedConnectionProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.ClusterNotRedundantWarning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.HighRamUsageWarning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.UnassignedShardsWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.ClusterNodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.version.ElasticsearchVersion;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.version.ElasticsearchVersionProvider;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import app.habitzl.elasticsearch.status.monitor.util.TimeFormatter;
import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class DefaultStatusAnalyserTest {

    private static final String TOOL_VERSION = null;
    private static final String CURRENT_TIME = Randoms.generateString("Timestamp: ");

    private DefaultStatusAnalyser sut;
    private StatusMonitorConfiguration configuration;
    private ElasticsearchClient elasticsearchClient;
    private ElasticsearchVersionProvider elasticsearchVersionProvider;
    private EndpointAnalyser endpointAnalyser;
    private ClusterAnalyser clusterAnalyser;
    private ShardAnalyser shardAnalyser;

    @BeforeEach
    void setUp() {
        Clock clock = Clocks.fixedSystemDefault();
        TimeFormatter timeFormatter = prepareTimeFormatter(clock);
        configuration = StatusMonitorConfigurations.random();
        elasticsearchClient = mock(ElasticsearchClient.class);
        elasticsearchVersionProvider = mockElasticsearchVersionProvider();
        AnalyserProvider analyserProvider = new AnalyserProvider(
                mockEndpointAnalyser(),
                mockClusterAnalyserProvider(),
                mockShardAnalyser()
        );
        sut = new DefaultStatusAnalyser(clock, timeFormatter, configuration, elasticsearchClient, elasticsearchVersionProvider, analyserProvider);
    }

    @ParameterizedTest
    @EnumSource(value = ConnectionStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "SUCCESS")
    void createReport_connectionStatusNotSuccess_doNotAttemptMoreStatusRequests(final ConnectionStatus connectionStatusNotSuccess) {
        // Given
        when(elasticsearchClient.checkConnection())
                .thenReturn(ConnectionInfo.error(connectionStatusNotSuccess, ""));

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
        when(elasticsearchClient.checkConnection())
                .thenReturn(ConnectionInfo.error(ConnectionStatus.NOT_FOUND, connectionErrorInformation));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        AnalysisReport expected = AnalysisReport.aborted(
                TOOL_VERSION,
                CURRENT_TIME,
                configuration,
                List.of(GeneralConnectionProblem.create(connectionErrorInformation))
        );
        assertThat(analysisReport, equalTo(expected));
    }

    @Test
    void createReport_connectionStatusSSLHandshakeFailure_returnAbortedAnalysisReportWithSSLHandshakeProblem() {
        // Given
        when(elasticsearchClient.checkConnection())
                .thenReturn(ConnectionInfo.error(ConnectionStatus.SSL_HANDSHAKE_FAILURE, ""));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        AnalysisReport expected = AnalysisReport.aborted(
                TOOL_VERSION,
                CURRENT_TIME,
                configuration,
                List.of(SSLHandshakeProblem.create())
        );
        assertThat(analysisReport, equalTo(expected));
    }

    @Test
    void createReport_connectionStatusUnauthorized_returnAbortedAnalysisReportWithUnauthorizedConnectionProblem() {
        // Given
        when(elasticsearchClient.checkConnection())
                .thenReturn(ConnectionInfo.error(ConnectionStatus.UNAUTHORIZED, ""));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        AnalysisReport expected = AnalysisReport.aborted(
                TOOL_VERSION,
                CURRENT_TIME,
                configuration,
                List.of(UnauthorizedConnectionProblem.create())
        );
        assertThat(analysisReport, equalTo(expected));
    }

    @Test
    void createReport_connectionStatusServiceUnavailable_returnAbortedAnalysisReportWithClusterNotFullyOperationalProblem() {
        // Given
        when(elasticsearchClient.checkConnection())
                .thenReturn(ConnectionInfo.error(ConnectionStatus.SERVICE_UNAVAILABLE, ""));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        AnalysisReport expected = AnalysisReport.aborted(
                TOOL_VERSION,
                CURRENT_TIME,
                configuration,
                List.of(ClusterNotFullyOperationalProblem.create())
        );
        assertThat(analysisReport, equalTo(expected));
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
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void createReport_allStatusRequestsSucceedWithWarnings_performStatusRequestsInOrder() {
        // Given
        givenAllRequestsSucceedWithWarnings(List.of());

        // When
        sut.createReport();

        // Then
        InOrder inOrder = inOrder(elasticsearchClient);
        inOrder.verify(elasticsearchClient).checkConnection();
        inOrder.verify(elasticsearchClient).getClusterInfo();
        inOrder.verify(elasticsearchClient).getNodeInfo();
        inOrder.verify(elasticsearchClient).getUnassignedShardInfo();
    }

    @Test
    void createReport_allStatusRequestsSucceed_updateElasticsearchVersion() {
        // Given
        ClusterNodeInfo clusterNodeInfo = ClusterNodeInfos.random();
        givenAllRequestsSucceed(ClusterInfos.random(), clusterNodeInfo, null);

        // When
        sut.createReport();

        // Then
        verify(elasticsearchVersionProvider).updateVersion(clusterNodeInfo.getElasticsearchVersion());
    }

    @Test
    void createReport_getNodeInfoRequestsFails_doNotUpdateElasticsearchVersion() {
        // Given
        givenAllRequestsSucceed();
        when(elasticsearchClient.getNodeInfo()).thenReturn(Optional.empty());

        // When
        sut.createReport();

        // Then
        verify(elasticsearchVersionProvider, never()).updateVersion(anyString());
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
    void createReport_shardAnalyserFindsWarning_returnExpectedAnalysisReport() {
        // Given
        Warning warning = givenShardAnalyserFindsWarning();
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
        Warning warning3 = givenShardAnalyserFindsWarning();
        AnalysisReport expectedAnalysisReport = givenAllRequestsSucceedWithWarnings(List.of(warning1, warning2, warning3));

        // When
        AnalysisReport analysisReport = sut.createReport();

        // Then
        assertThat(analysisReport, equalTo(expectedAnalysisReport));
    }

    private TimeFormatter prepareTimeFormatter(final Clock clock) {
        TimeFormatter timeFormatter = mock(TimeFormatter.class);
        when(timeFormatter.format(clock.instant())).thenReturn(CURRENT_TIME);
        return timeFormatter;
    }

    private ElasticsearchVersionProvider mockElasticsearchVersionProvider() {
        elasticsearchVersionProvider = mock(ElasticsearchVersionProvider.class);
        when(elasticsearchVersionProvider.get()).thenReturn(ElasticsearchVersion.defaultVersion());
        return elasticsearchVersionProvider;
    }

    private EndpointAnalyser mockEndpointAnalyser() {
        endpointAnalyser = mock(EndpointAnalyser.class);
        when(endpointAnalyser.analyse(anyList())).thenReturn(AnalysisResult.empty());
        return endpointAnalyser;
    }

    private ClusterAnalyserProvider mockClusterAnalyserProvider() {
        ClusterAnalyserProvider clusterAnalyserProvider = mock(ClusterAnalyserProvider.class);
        clusterAnalyser = mock(ClusterAnalyser.class);
        when(clusterAnalyser.analyse(any(ClusterSettings.class), anyList())).thenReturn(AnalysisResult.empty());
        when(clusterAnalyserProvider.get()).thenReturn(clusterAnalyser);
        return clusterAnalyserProvider;
    }

    private ShardAnalyser mockShardAnalyser() {
        shardAnalyser = mock(ShardAnalyser.class);
        when(shardAnalyser.analyse(nullable(UnassignedShardInfo.class))).thenReturn(AnalysisResult.empty());
        return shardAnalyser;
    }

    /**
     * Returns the expected status report from all status monitor requests.
     */
    private AnalysisReport givenAllRequestsSucceed() {
        ClusterInfo clusterInfo = ClusterInfos.randomHealthy();
        ClusterNodeInfo clusterNodeInfo = ClusterNodeInfos.random();
        givenAllRequestsSucceed(clusterInfo, clusterNodeInfo, null);
        return AnalysisReport.finished(TOOL_VERSION, CURRENT_TIME, configuration, List.of(), List.of(), clusterInfo, clusterNodeInfo.getNodeInfos());
    }

    /**
     * Returns the expected status report from all status monitor requests.
     */
    private AnalysisReport givenAllRequestsSucceedWithWarnings(final List<Warning> warnings) {
        ClusterInfo clusterInfo = ClusterInfos.random();
        ClusterNodeInfo clusterNodeInfo = ClusterNodeInfos.random();
        UnassignedShardInfo unassignedShardInfo = UnassignedShardInfos.random();
        givenAllRequestsSucceed(clusterInfo, clusterNodeInfo, unassignedShardInfo);
        return AnalysisReport.finished(TOOL_VERSION, CURRENT_TIME, configuration, List.of(), warnings, clusterInfo, clusterNodeInfo.getNodeInfos());
    }

    private void givenAllRequestsSucceed(
            final ClusterInfo clusterInfo,
            final ClusterNodeInfo clusterNodeInfo,
            final @Nullable UnassignedShardInfo unassignedShardInfo) {
        when(elasticsearchClient.checkConnection()).thenReturn(ConnectionInfo.success());
        when(elasticsearchClient.getClusterInfo()).thenReturn(Optional.of(clusterInfo));
        when(elasticsearchClient.getNodeInfo()).thenReturn(Optional.of(clusterNodeInfo));
        when(elasticsearchClient.getUnassignedShardInfo()).thenReturn(Optional.ofNullable(unassignedShardInfo));
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

    private Warning givenShardAnalyserFindsWarning() {
        UnassignedShardsWarning warning = UnassignedShardsWarning.create(UnassignedShardInfos.random());
        when(shardAnalyser.analyse(any(UnassignedShardInfo.class)))
                .thenReturn(AnalysisResult.create(List.of(), List.of(warning)));
        return warning;
    }
}