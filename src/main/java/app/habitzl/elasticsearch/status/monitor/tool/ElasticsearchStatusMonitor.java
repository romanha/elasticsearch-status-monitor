package app.habitzl.elasticsearch.status.monitor.tool;

import app.habitzl.elasticsearch.status.monitor.StatusMonitor;
import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.params.NodeParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElasticsearchStatusMonitor implements StatusMonitor {

	private static final Logger LOG = LogManager.getLogger(ElasticsearchStatusMonitor.class);
	private static final String METHOD_GET = "GET";
	private static final String HEADER_ACCEPT = "accept";
	private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

	private final RestHighLevelClient client;
	private final ResponseMapper mapper;
	private final InfoParser parser;

	@Inject
	public ElasticsearchStatusMonitor(final RestHighLevelClient client, final ResponseMapper mapper, final InfoParser parser) {
		this.client = client;
		this.mapper = mapper;
		this.parser = parser;
	}

	@Override
	public ClusterInfo getClusterInfo() {
		ClusterInfo clusterHealth;

		ClusterHealthRequest request = new ClusterHealthRequest();
		try {
			ClusterHealthResponse response = client.cluster().health(request, RequestOptions.DEFAULT);
			clusterHealth = ClusterInfo.fromClusterHealthResponse(response);
		} catch (IOException e) {
			clusterHealth = ClusterInfo.unknown();
			logConnectionError(e);
		}

		return clusterHealth;
	}

	@Override
	public List<NodeInfo> getNodeInfo() {
		List<NodeInfo> nodeInfos = new ArrayList<>();

		Request request = new Request(METHOD_GET, "/_cat/nodes");
		setAcceptedContentToJSON(request);
		request.addParameter("h", NodeParams.all());

		try {
			Response response = client.getLowLevelClient().performRequest(request);
			List<Map<String, Object>> result = mapper.toMaps(response);

			for (Map<String, Object> map : result) {
				NodeInfo nodeInfo = parser.parseNodeInfo(map);
				nodeInfos.add(nodeInfo);
				LOG.debug("Parsed node info: {}", nodeInfo);
			}
		} catch (IOException e) {
			logConnectionError(e);
		}

		return nodeInfos;
	}

	/**
	 * TODO remove as the client is closed anyway by the main teardown logic
	 */
	@Override
	public void closeConnection() throws IOException {
		client.close();
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

	private void logConnectionError(final Exception e) {
		LOG.error("Failed to get cluster information!", e);
	}
}
