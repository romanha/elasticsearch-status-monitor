package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

import java.util.Map;

/**
 * A mapper for data returned via the Elasticsearch {@code /_nodes} and {@code /_nodes/stats} APIs.
 */
public interface NodeInfoMapper {
    @Deprecated(forRemoval = true)
    NodeInfo map(Map<String, Object> data);

    NodeInfo map(String nodeInfoJson, String nodeStatsJson);
}
