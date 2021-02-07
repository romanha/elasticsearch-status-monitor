package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;

/**
 * A mapper for data returned via the Elasticsearch {@code /_cluster/allocation/explain} API.
 */
public interface ClusterAllocationMapper {
    UnassignedShardInfo map(String jsonData);
}
