package app.habitzl.elasticsearch.status.monitor.tool.client;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.ElasticsearchClient;
import app.habitzl.elasticsearch.status.monitor.tool.client.connection.MonitoringRestClient;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.ClusterNodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.ClusterAllocationParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.ClusterHealthParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.ClusterSettingsParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.ClusterStateParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.ClusterStatsParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.EndpointVersionParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.GeneralParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.NodeInfoParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.NodeStatsParams;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.net.ssl.SSLHandshakeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;

public class DefaultElasticsearchClient implements ElasticsearchClient {
    private static final Logger LOG = LogManager.getLogger(DefaultElasticsearchClient.class);

    static final String METHOD_GET = "GET";
    static final String HEADER_ACCEPT = "accept";
    static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private static final int HTTP_STATUS_BAD_REQUEST = 400;

    private final RestClient client;
    private final ResponseMapper responseMapper;
    private final InfoMapper infoMapper;

    @Inject
    public DefaultElasticsearchClient(
            final @MonitoringRestClient RestClient client,
            final ResponseMapper responseMapper,
            final InfoMapper infoMapper) {
        this.client = client;
        this.responseMapper = responseMapper;
        this.infoMapper = infoMapper;
    }

    @Override
    public ConnectionInfo checkConnection() {
        ConnectionInfo connectionInfo;

        Request request = new Request(METHOD_GET, EndpointVersionParams.API_ENDPOINT);
        setAcceptedContentToJSON(request);

        try {
            Response response = client.performRequest(request);
            ConnectionStatus status = ConnectionStatus.fromHttpCode(response.getStatusLine().getStatusCode());
            connectionInfo = status == ConnectionStatus.SUCCESS
                    ? ConnectionInfo.success()
                    : ConnectionInfo.error(status, response.getStatusLine().toString());
        } catch (final ResponseException e) {
            logError(e);
            Response response = e.getResponse();
            ConnectionStatus status = ConnectionStatus.fromHttpCode(response.getStatusLine().getStatusCode());
            connectionInfo = ConnectionInfo.error(status, response.getStatusLine().toString());
        } catch (final SSLHandshakeException e) {
            logError(e);
            connectionInfo = ConnectionInfo.error(ConnectionStatus.SSL_HANDSHAKE_FAILURE, e.getMessage());
        } catch (final IOException e) {
            logError(e);
            connectionInfo = ConnectionInfo.error(ConnectionStatus.NOT_FOUND, e.getMessage());
        }

        return connectionInfo;
    }

    @Override
    public Optional<ClusterSettings> getClusterSettings() {
        ClusterSettings clusterSettings;

        Request request = new Request(METHOD_GET, ClusterSettingsParams.API_ENDPOINT);
        request.addParameter(ClusterSettingsParams.PARAM_INCLUDE_DEFAULTS, "true");

        try {
            String result = sendRequestAndMapJSONResponse(request);
            clusterSettings = infoMapper.mapClusterSettings(result);
            LOG.debug("Mapped cluster settings: {}", clusterSettings);
        } catch (final IOException e) {
            logError(e);
            clusterSettings = null;
        }

        return Optional.ofNullable(clusterSettings);
    }

    @Override
    public Optional<ClusterInfo> getClusterInfo() {
        ClusterInfo clusterInfo;

        Request clusterHealthRequest = new Request(METHOD_GET, ClusterHealthParams.API_ENDPOINT);
        Request clusterStateRequest = new Request(METHOD_GET, ClusterStateParams.API_ENDPOINT);
        Request clusterStatsRequest = new Request(METHOD_GET, ClusterStatsParams.API_ENDPOINT);

        try {
            String clusterHealthResult = sendRequestAndMapJSONResponse(clusterHealthRequest);
            String clusterStateResult = sendRequestAndMapJSONResponse(clusterStateRequest);
            String clusterStatsResult = sendRequestAndMapJSONResponse(clusterStatsRequest);

            clusterInfo = infoMapper.mapClusterInfo(clusterHealthResult, clusterStateResult, clusterStatsResult);
            LOG.debug("Mapped cluster info: {}", clusterInfo);
        } catch (final IOException e) {
            logError(e);
            clusterInfo = null;
        }

        return Optional.ofNullable(clusterInfo);
    }

    @Override
    public Optional<ClusterNodeInfo> getNodeInfo() {
        ClusterNodeInfo result = null;

        Request masterNodeRequest = new Request(METHOD_GET, ClusterStateParams.onlyRequestMasterNode());
        Request nodeInfoRequest = new Request(METHOD_GET, NodeInfoParams.API_ENDPOINT);
        Request nodeStatsRequest = new Request(METHOD_GET, NodeStatsParams.API_ENDPOINT);
        nodeStatsRequest.addParameter(NodeStatsParams.PARAM_METRIC, NodeStatsParams.allMetrics());

        try {
            String masterNodeResult = sendRequestAndMapJSONResponse(masterNodeRequest);
            String nodeInfoResult = sendRequestAndMapJSONResponse(nodeInfoRequest);
            String nodeStatsResult = sendRequestAndMapJSONResponse(nodeStatsRequest);

            List<NodeInfo> nodeInfos = infoMapper.mapNodeInfo(masterNodeResult, nodeInfoResult, nodeStatsResult);
            LOG.debug("Mapped node infos: {}", nodeInfos);
            result = new ClusterNodeInfo(nodeInfos);
        } catch (final IOException | IllegalArgumentException e) {
            logError(e);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<UnassignedShardInfo> getUnassignedShardInfo() {
        UnassignedShardInfo unassignedShardInfo;

        Request request = new Request(METHOD_GET, ClusterAllocationParams.API_ENDPOINT);
        setAcceptedContentToJSON(request);

        try {
            Response response = client.performRequest(request);
            if (response.getStatusLine().getStatusCode() != HTTP_STATUS_BAD_REQUEST) {
                String result = responseMapper.getContentAsString(response);
                unassignedShardInfo = infoMapper.mapUnassignedShardInfo(result);
                LOG.debug("Mapped unassigned shard info: {}", unassignedShardInfo);
            } else {
                unassignedShardInfo = null;
                LOG.debug("No unassigned shards found.");
            }
        } catch (final ResponseException e) {
            unassignedShardInfo = null;
            if (e.getResponse().getStatusLine().getStatusCode() == HTTP_STATUS_BAD_REQUEST) {
                LOG.debug("No unassigned shards found.");
            } else {
                logError(e);
            }
        } catch (final IOException e) {
            logError(e);
            unassignedShardInfo = null;
        }

        return Optional.ofNullable(unassignedShardInfo);
    }

    private String sendRequestAndMapJSONResponse(final Request request) throws IOException {
        setAcceptedContentToJSON(request);
        Response response = client.performRequest(request);
        return responseMapper.getContentAsString(response);
    }

    /**
     * Either setting the "accept" header or the "format" parameter would also work alone.
     */
    private void setAcceptedContentToJSON(final Request request) {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader(HEADER_ACCEPT, CONTENT_TYPE_APPLICATION_JSON);
        request.setOptions(builder);

        request.addParameter(GeneralParams.PARAM_FORMAT, "json");
    }

    private void logError(final Exception e) {
        LOG.error("Failed to request Elasticsearch data!", e);
    }
}
