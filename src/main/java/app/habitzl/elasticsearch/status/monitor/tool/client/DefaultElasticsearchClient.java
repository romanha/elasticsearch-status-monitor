package app.habitzl.elasticsearch.status.monitor.tool.client;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.ElasticsearchClient;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.RestStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.NodeParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;

import javax.inject.Inject;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultElasticsearchClient implements ElasticsearchClient {
    private static final Logger LOG = LogManager.getLogger(DefaultElasticsearchClient.class);

    private static final String METHOD_GET = "GET";
    private static final String HEADER_ACCEPT = "accept";
    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    private final RestClient client;
    private final ResponseMapper responseMapper;
    private final InfoMapper infoMapper;

    @Inject
    public DefaultElasticsearchClient(final RestClient client, final ResponseMapper responseMapper, final InfoMapper infoMapper) {
        this.client = client;
        this.responseMapper = responseMapper;
        this.infoMapper = infoMapper;
    }

    @Override
    public ConnectionInfo checkConnection() {
        ConnectionInfo connectionInfo;

        Request request = new Request(METHOD_GET, "/_cluster/health");
        setAcceptedContentToJSON(request);

        try {
            client.performRequest(request);
            connectionInfo = ConnectionInfo.success();
        } catch (final ResponseException e) {
            logError(e);
            RestStatus restStatus = RestStatus.fromHttpCode(e.getResponse().getStatusLine().getStatusCode());
            if (restStatus == RestStatus.UNAUTHORIZED) {
                connectionInfo = ConnectionInfo.error(ConnectionStatus.UNAUTHORIZED, null);
            } else {
                connectionInfo = ConnectionInfo.error(ConnectionStatus.UNKNOWN, e.getMessage());
            }
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

        Request request = new Request(METHOD_GET, "/_cluster/settings");
        request.addParameter("include_defaults", "true");

        try {
            Response response = client.performRequest(request);
            String result = responseMapper.getContentAsString(response);
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

        Request request = new Request(METHOD_GET, "/_cluster/health");
        setAcceptedContentToJSON(request);

        try {
            Response response = client.performRequest(request);
            String result = responseMapper.getContentAsString(response);
            clusterInfo = infoMapper.mapClusterInfo(result);
        } catch (final IOException e) {
            logError(e);
            clusterInfo = null;
        }

        return Optional.ofNullable(clusterInfo);
    }

    @Override
    public List<NodeInfo> getNodeInfo() {
        List<NodeInfo> nodeInfos = new ArrayList<>();

        Request request = new Request(METHOD_GET, "/_cat/nodes");
        setAcceptedContentToJSON(request);
        request.addParameter("h", NodeParams.all());
//		request.addParameter("full_id", "true");

        try {
            Response response = client.performRequest(request);
            List<Map<String, Object>> result = responseMapper.toMaps(response);

            for (Map<String, Object> map : result) {
                NodeInfo nodeInfo = infoMapper.mapNodeInfo(map);
                nodeInfos.add(nodeInfo);
                LOG.debug("Mapped node info: {}", nodeInfo);
            }
        } catch (final IOException e) {
            logError(e);
        }

        return nodeInfos;
    }

    /**
     * Either setting the "accept" header or the "format" parameter would also work alone.
     */
    private void setAcceptedContentToJSON(final Request request) {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader(HEADER_ACCEPT, CONTENT_TYPE_APPLICATION_JSON);
        request.setOptions(builder);

        request.addParameter("format", "json");
    }

    private void logError(final Exception e) {
        LOG.error("Failed to get cluster information!", e);
    }
}
