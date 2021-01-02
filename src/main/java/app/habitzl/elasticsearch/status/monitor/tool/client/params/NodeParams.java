package app.habitzl.elasticsearch.status.monitor.tool.client.params;

import java.util.StringJoiner;

/**
 * Parameter list of all values offered by the Elasticsearch {@code /_cat/nodes} API.
 */
public final class NodeParams {
	private NodeParams() {
		// instantiation protection
	}

	public static final String IP_KEY = "ip";
	public static final String RAM_PERCENT_KEY = "ram.percent";
	public static final String HEAP_PERCENT_KEY = "heap.percent";

	public static final String NODE_PROCESS_ID = "pid";
	public static final String NODE_ID_KEY = "nodeId";
	public static final String NODE_NAME_KEY = "name";
	public static final String NODE_MASTER_KEY = "master";
	public static final String NODE_ROLE_KEY = "node.role";
	public static final String NODE_UPTIME = "uptime";
	public static final String AVERAGE_LOAD_KEY = "load_15m";

	public static String all() {
		return new StringJoiner(",")
				.add(IP_KEY)
				.add(RAM_PERCENT_KEY)
				.add(HEAP_PERCENT_KEY)
				.add(NODE_PROCESS_ID)
				.add(NODE_ID_KEY)
				.add(NODE_NAME_KEY)
				.add(NODE_MASTER_KEY)
				.add(NODE_ROLE_KEY)
				.add(NODE_UPTIME)
				.add(AVERAGE_LOAD_KEY)
				.toString();
	}
}
