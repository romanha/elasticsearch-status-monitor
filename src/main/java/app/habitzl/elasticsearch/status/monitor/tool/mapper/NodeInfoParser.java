package app.habitzl.elasticsearch.status.monitor.tool.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

import java.util.Map;

/**
 * A parser for data returned via the Elasticsearch {@code /_cat/nodes} API.
 */
public interface NodeInfoParser {
	NodeInfo parse(Map<String, Object> data);
}
