package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

import java.util.Map;

/**
 * A mapper for data returned via the Elasticsearch {@code /_cat/nodes} API.
 */
public interface NodeInfoMapper {
	NodeInfo map(Map<String, Object> data);
}
