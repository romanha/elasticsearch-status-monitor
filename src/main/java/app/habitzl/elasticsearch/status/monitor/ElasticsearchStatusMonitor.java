package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.data.ClusterHealth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public class ElasticsearchStatusMonitor implements StatusMonitor {

	private static final Logger LOG = LogManager.getLogger(ElasticsearchStatusMonitor.class);

	private final RestHighLevelClient client;

	@Inject
	public ElasticsearchStatusMonitor(final RestHighLevelClient client) {
		this.client = client;
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
			LOG.error("Failed to get cluster health!", e);
		}

		return clusterHealth;
	}
}
