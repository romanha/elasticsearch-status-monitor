package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Map;

import static app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils.MapUtils.*;
import static app.habitzl.elasticsearch.status.monitor.tool.client.params.NodeParams.*;

public class DefaultNodeInfoMapper implements NodeInfoMapper {
	static final String MASTER_NODE_MARKER = "*";
	static final String DATA_NODE_ROLE_ID = "d";
	static final String MASTER_ELIGIBLE_NODE_ROLE_ID = "m";

	private final TimeParser timeParser;
	private final TimeFormatter timeFormatter;

	@Inject
	public DefaultNodeInfoMapper(final TimeParser timeParser, final TimeFormatter timeFormatter) {
		this.timeParser = timeParser;
		this.timeFormatter = timeFormatter;
	}

	@Override
	public NodeInfo map(final Map<String, Object> data) {
		EndpointInfo endpointInfo = mapEndpointInfo(data);

		String processId = getString(data, NODE_PROCESS_ID);
		String nodeId = getString(data, NODE_ID_KEY);
		String name = getString(data, NODE_NAME_KEY);
		boolean isMasterNode = (getString(data, NODE_MASTER_KEY)).equalsIgnoreCase(MASTER_NODE_MARKER);
		boolean isDataNode = (getString(data, NODE_ROLE_KEY)).contains(DATA_NODE_ROLE_ID);
		boolean isMasterEligibleNode = (getString(data, NODE_ROLE_KEY)).contains(MASTER_ELIGIBLE_NODE_ROLE_ID);
		Duration uptime = timeParser.parse(getString(data, NODE_UPTIME));
		float load15m = getFloat(data, AVERAGE_LOAD_KEY);
		return new NodeInfo(
				processId,
				nodeId,
				name,
				isMasterNode,
				isDataNode,
				isMasterEligibleNode,
				timeFormatter.format(uptime),
				load15m,
				endpointInfo
		);
	}

	private EndpointInfo mapEndpointInfo(final Map<String, Object> data) {
		String address = getString(data, IP_KEY);
		int ram = getInteger(data, RAM_PERCENT_KEY);
		int heap = getInteger(data, HEAP_PERCENT_KEY);

		return new EndpointInfo(address, ram, heap);
	}
}