package app.habitzl.elasticsearch.status.monitor.tool;

import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

import java.util.Map;

/**
 * A parser for data returned via various Elasticsearch APIs.
 * This interface acts as a facade for delegating parsing to more specialised parsers.
 */
public interface InfoParser {

	/**
	 * Parses data returned via the ES {@code /_cat/nodes} API.
	 */
	NodeInfo parseNodeInfo(Map<String, Object> data);
}
