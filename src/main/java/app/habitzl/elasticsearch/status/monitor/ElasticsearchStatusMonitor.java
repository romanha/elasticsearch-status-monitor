package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.data.cluster.ClusterHealth;
import app.habitzl.elasticsearch.status.monitor.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.mapper.NodeInfoParser;
import app.habitzl.elasticsearch.status.monitor.mapper.ResponseMapper;
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

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public class ElasticsearchStatusMonitor implements StatusMonitor {

	private static final Logger LOG = LogManager.getLogger(ElasticsearchStatusMonitor.class);
	private static final String GET_METHOD = "GET";
	private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

	private final RestHighLevelClient client;
	private final ResponseMapper mapper;
	private final NodeInfoParser nodeInfoParser;

	@Inject
	public ElasticsearchStatusMonitor(
			final RestHighLevelClient client,
			final ResponseMapper mapper,
			final NodeInfoParser nodeInfoParser) {
		this.client = client;
		this.mapper = mapper;
		this.nodeInfoParser = nodeInfoParser;
	}

	@Override
	public ClusterHealth getClusterHealth() {
		ClusterHealth clusterHealth;

		ClusterHealthRequest request = new ClusterHealthRequest();
		try {
			ClusterHealthResponse response = client.cluster().health(request, RequestOptions.DEFAULT);
			clusterHealth = ClusterHealth.fromClusterHealthResponse(response);
		} catch (IOException e) {
			clusterHealth = ClusterHealth.unknown();
			logConnectionError(e);
		}

		return clusterHealth;
	}

	@Override
	public List<NodeInfo> getNodeHealth() {
		List<NodeInfo> nodeInfos = new ArrayList<>();

		Request request = new Request(GET_METHOD, "/_cat/nodes");
		setAcceptedContentToJSON(request);
		try {
			Response response = client.getLowLevelClient().performRequest(request);
			List<Map<String, Object>> result = mapper.toMaps(response);

			for (Map<String, Object> map : result) {
				NodeInfo nodeInfo = nodeInfoParser.parse(map);
				nodeInfos.add(nodeInfo);
				LOG.debug("Received node info: {}", nodeInfo);
			}
		} catch (IOException e) {
			logConnectionError(e);
		}

		return nodeInfos;
	}

	private void setAcceptedContentToJSON(final Request request) {
		RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
		builder.addHeader("accept", CONTENT_TYPE_APPLICATION_JSON);
		request.setOptions(builder);

		request.addParameter("format", "json");
	}

	private void logConnectionError(final Exception e) {
		LOG.error("Failed to get cluster information!", e);
	}
}
