package app.habitzl.elasticsearch.status.monitor.mapper;

import app.habitzl.elasticsearch.status.monitor.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.data.node.NodeInfo;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public class FromMapNodeInfoParser implements NodeInfoParser {

	private static final String IP_KEY = "ip";
	private static final String HEAP_PERCENT_KEY = "heap.percent";
	private static final String RAM_PERCENT_KEY = "ram.percent";
	private static final String NODE_NAME_KEY = "name";
	private static final String NODE_MASTER_KEY = "master";
	private static final String NODE_ROLE_KEY = "node.role";
	private static final String AVERAGE_LOAD_KEY = "load_15m";

	private static final String MASTER_NODE_MARKER = "*";
	private static final String DATA_NODE_ROLE_ID = "d";
	private static final String MASTER_ELIGIBLE_NODE_ROLE_ID = "m";

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
		int heap = getInteger(data, HEAP_PERCENT_KEY);
		int ram = getInteger(data, RAM_PERCENT_KEY);

		return new EndpointInfo(address, ram, heap);
	}

	private int getInteger(final Map<String, Object> data, final String key) {
		String stringValue = getString(data, key);
		return Objects.isNull(stringValue) ? 0 : Integer.parseInt(stringValue);
	}

	private float getFloat(final Map<String, Object> data, final String key) {
		String stringValue = getString(data, key);
		return Objects.isNull(stringValue) ? 0.0f : Float.parseFloat(stringValue);
	}

	private String getString(final Map<String, Object> data, final String key) {
		return (String) data.get(key);
	}
}
