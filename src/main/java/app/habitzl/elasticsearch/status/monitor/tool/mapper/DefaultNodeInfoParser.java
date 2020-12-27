package app.habitzl.elasticsearch.status.monitor.tool.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Map;

import static app.habitzl.elasticsearch.status.monitor.tool.mapper.utils.MapUtils.*;
import static app.habitzl.elasticsearch.status.monitor.tool.params.NodeParams.*;

public class DefaultNodeInfoParser implements NodeInfoParser {
	static final String MASTER_NODE_MARKER = "*";
	static final String DATA_NODE_ROLE_ID = "d";
	static final String MASTER_ELIGIBLE_NODE_ROLE_ID = "m";

	private final TimeParser timeParser;

	@Inject
	public DefaultNodeInfoParser(final TimeParser timeParser) {
		this.timeParser = timeParser;
	}

	@Override
	public NodeInfo parse(final Map<String, Object> data) {
		EndpointInfo endpointInfo = parseEndpointInfo(data);

		String processId = getString(data, NODE_PROCESS_ID);
		String name = getString(data, NODE_NAME_KEY);
		boolean isMasterNode = (getString(data, NODE_MASTER_KEY)).equalsIgnoreCase(MASTER_NODE_MARKER);
		boolean isDataNode = (getString(data, NODE_ROLE_KEY)).contains(DATA_NODE_ROLE_ID);
		boolean isMasterEligibleNode = (getString(data, NODE_ROLE_KEY)).contains(MASTER_ELIGIBLE_NODE_ROLE_ID);
		Duration uptime = timeParser.parse(getString(data, NODE_UPTIME));
		float load15m = getFloat(data, AVERAGE_LOAD_KEY);
		return new NodeInfo(
				processId,
				name,
				isMasterNode,
				isDataNode,
				isMasterEligibleNode,
				uptime,
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
