package app.habitzl.elasticsearch.status.monitor.tool.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.InfoParser;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

import javax.inject.Inject;
import java.util.Map;

public class DefaultInfoParser implements InfoParser {

	private final NodeInfoParser nodeInfoParser;

	@Inject
	public DefaultInfoParser(final NodeInfoParser nodeInfoParser) {
		this.nodeInfoParser = nodeInfoParser;
	}

	@Override
	public NodeInfo parseNodeInfo(final Map<String, Object> data) {
		return nodeInfoParser.parse(data);
	}
}
