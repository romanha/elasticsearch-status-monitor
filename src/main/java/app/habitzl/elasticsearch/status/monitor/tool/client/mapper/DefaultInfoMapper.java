package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.InfoMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

import javax.inject.Inject;
import java.util.Map;

public class DefaultInfoMapper implements InfoMapper {

	private final NodeInfoMapper nodeInfoMapper;

	@Inject
	public DefaultInfoMapper(final NodeInfoMapper nodeInfoMapper) {
		this.nodeInfoMapper = nodeInfoMapper;
	}

	@Override
	public NodeInfo mapNodeInfo(final Map<String, Object> data) {
		return nodeInfoMapper.map(data);
	}
}
