package app.habitzl.elasticsearch.status.monitor.tool;

import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

import java.util.Map;

/**
 * A mapper for data returned via various Elasticsearch APIs.
 * This interface acts as a facade for delegating parsing to more specialised mappers.
 */
public interface InfoMapper {

	/**
	 * Maps data returned via the ES {@code /_cat/nodes} API.
	 */
	NodeInfo mapNodeInfo(Map<String, Object> data);
}
