package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.data.cluster.ClusterHealth;
import app.habitzl.elasticsearch.status.monitor.data.node.NodeInfo;

import java.util.List;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public interface StatusMonitor {

	ClusterHealth getClusterHealth();

	List<NodeInfo> getNodeHealth();
}
