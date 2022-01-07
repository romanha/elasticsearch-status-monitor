package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;

/**
 * A mapper for data returned via the Elasticsearch {@code /_cluster/health}, {@code /_cluster/state} and {@code /_cluster/stats} APIs.
 */
public interface ClusterInfoMapper {
    ClusterInfo map(String clusterHealthJson, String clusterStateJson, String clusterStatsJson);
}
