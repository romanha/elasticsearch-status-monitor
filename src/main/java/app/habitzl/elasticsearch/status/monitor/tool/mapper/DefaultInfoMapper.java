package app.habitzl.elasticsearch.status.monitor.tool.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.InfoMapper;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

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
