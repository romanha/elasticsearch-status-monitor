package app.habitzl.elasticsearch.status.monitor.tool.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.NodeInfoParser;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

import java.util.Map;

import static app.habitzl.elasticsearch.status.monitor.tool.mapper.utils.MapUtils.*;

public class DefaultNodeInfoParser implements NodeInfoParser {

	static final String IP_KEY = "ip";
	static final String RAM_PERCENT_KEY = "ram.percent";
	static final String HEAP_PERCENT_KEY = "heap.percent";

	static final String NODE_NAME_KEY = "name";
	static final String NODE_MASTER_KEY = "master";
	static final String NODE_ROLE_KEY = "node.role";
	static final String AVERAGE_LOAD_KEY = "load_15m";

	static final String MASTER_NODE_MARKER = "*";
	static final String DATA_NODE_ROLE_ID = "d";
	static final String MASTER_ELIGIBLE_NODE_ROLE_ID = "m";

	@Override
	public NodeInfo parse(final Map<String, Object> data) {
		EndpointInfo endpointInfo = parseEndpointInfo(data);

		String name = getString(data, NODE_NAME_KEY);
		boolean isMasterNode = (getString(data, NODE_MASTER_KEY)).equalsIgnoreCase(MASTER_NODE_MARKER);
		boolean isDataNode = (getString(data, NODE_ROLE_KEY)).contains(DATA_NODE_ROLE_ID);
		boolean isMasterEligibleNode = (getString(data, NODE_ROLE_KEY)).contains(MASTER_ELIGIBLE_NODE_ROLE_ID);
		float load15m = getFloat(data, AVERAGE_LOAD_KEY);
		return new NodeInfo(
				name,
				isMasterNode,
				isDataNode,
				isMasterEligibleNode,
				load15m,
				endpointInfo
		);
	}

	private EndpointInfo parseEndpointInfo(final Map<String, Object> data) {
		String address = getString(data, IP_KEY);
		int ram = getInteger(data, RAM_PERCENT_KEY);
		int heap = getInteger(data, HEAP_PERCENT_KEY);

		return new EndpointInfo(address, ram, heap);
	}
}
