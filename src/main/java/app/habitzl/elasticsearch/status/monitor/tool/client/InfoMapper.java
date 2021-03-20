package app.habitzl.elasticsearch.status.monitor.tool.client;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;

import java.util.List;

/**
 * A mapper for data returned via various Elasticsearch APIs.
 * This interface acts as a facade for delegating parsing to more specialised mappers.
 */
public interface InfoMapper {

    /**
     * Maps data returned via the ES {@code /_cluster/settings} API.
     */
    ClusterSettings mapClusterSettings(String jsonData);

    /**
     * Maps data returned via the ES {@code /_cluster/health} and {@code /_cluster/state} APIs.
     */
    ClusterInfo mapClusterInfo(String clusterHealthJson, String clusterStateJson);

    /**
     * Maps data returned via the ES {@code /_nodes} and {@code /_nodes/stats} APIs.
     */
    List<NodeInfo> mapNodeInfo(String nodeInfoJson, String nodeStatsJson);

    /**
     * Maps data returned via the ES {@code /_cluster/allocation/explain} API.
     */
    UnassignedShardInfo mapUnassignedShardInfo(String jsonData);
}
