package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;

/**
 * A mapper for data returned via the Elasticsearch {@code /_cluster/health} API.
 */
public interface ClusterInfoMapper {
    ClusterInfo map(String jsonData);
}
