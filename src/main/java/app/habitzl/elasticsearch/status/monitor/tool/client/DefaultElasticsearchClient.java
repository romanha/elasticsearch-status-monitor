package app.habitzl.elasticsearch.status.monitor.tool.client;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.ElasticsearchClient;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.NodeParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;

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

	private final RestHighLevelClient client;
	private final ResponseMapper responseMapper;
	private final InfoMapper infoMapper;

	@Inject
	public DefaultElasticsearchClient(final RestHighLevelClient client, final ResponseMapper responseMapper, final InfoMapper infoMapper) {
		this.client = client;
		this.responseMapper = responseMapper;
		this.infoMapper = infoMapper;
	}

	@Override
	public ConnectionInfo checkConnection() {
		ConnectionInfo connectionInfo;
		ClusterHealthRequest request = new ClusterHealthRequest();
		try {
			ClusterHealthResponse response = client.cluster().health(request, RequestOptions.DEFAULT);
			connectionInfo = ConnectionInfo.success(response.status());
		} catch (final SSLHandshakeException e) {
			logError(e);
			connectionInfo = ConnectionInfo.error(ConnectionStatus.SSL_HANDSHAKE_FAILURE);
		} catch (final ElasticsearchStatusException e) {
			logError(e);
			connectionInfo = ConnectionInfo.success(e.status());
		} catch (final IOException e) {
			logError(e);
			connectionInfo = ConnectionInfo.error(ConnectionStatus.NOT_FOUND);
		}

		return connectionInfo;
	}

	@Override
	public Optional<ClusterInfo> getClusterInfo() {
		ClusterInfo clusterInfo;

		ClusterHealthRequest request = new ClusterHealthRequest();
		try {
			ClusterHealthResponse response = client.cluster().health(request, RequestOptions.DEFAULT);
			clusterInfo = ClusterInfo.fromClusterHealthResponse(response);
		} catch (final IOException | ElasticsearchStatusException e) {
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
			Response response = client.getLowLevelClient().performRequest(request);
			List<Map<String, Object>> result = responseMapper.toMaps(response);

			for (Map<String, Object> map : result) {
				NodeInfo nodeInfo = infoMapper.mapNodeInfo(map);
				nodeInfos.add(nodeInfo);
				LOG.debug("Mapped node info: {}", nodeInfo);
			}
		} catch (final IOException | ElasticsearchStatusException e) {
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
