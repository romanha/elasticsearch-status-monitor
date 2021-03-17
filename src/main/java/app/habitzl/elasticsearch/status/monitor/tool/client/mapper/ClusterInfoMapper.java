package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;

/**
 * A mapper for data returned via the Elasticsearch {@code /_cluster/health} and {@code /_cluster/state} APIs.
 */
public interface ClusterInfoMapper {
    ClusterInfo map(String clusterHealthJson, String clusterStateJson);
}
