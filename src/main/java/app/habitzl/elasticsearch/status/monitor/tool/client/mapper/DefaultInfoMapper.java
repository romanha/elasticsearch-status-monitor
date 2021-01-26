package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.InfoMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

import javax.inject.Inject;
import java.util.Map;

public class DefaultInfoMapper implements InfoMapper {

    private final ClusterSettingsMapper clusterSettingsMapper;
    private final ClusterInfoMapper clusterInfoMapper;
    private final NodeInfoMapper nodeInfoMapper;

    @Inject
    public DefaultInfoMapper(
            final ClusterSettingsMapper clusterSettingsMapper,
            final ClusterInfoMapper clusterInfoMapper,
            final NodeInfoMapper nodeInfoMapper) {
        this.clusterSettingsMapper = clusterSettingsMapper;
        this.clusterInfoMapper = clusterInfoMapper;
        this.nodeInfoMapper = nodeInfoMapper;
    }

    @Override
    public ClusterSettings mapClusterSettings(final String jsonData) {
        return clusterSettingsMapper.map(jsonData);
    }

    @Override
    public ClusterInfo mapClusterInfo(final String jsonData) {
        return clusterInfoMapper.map(jsonData);
    }

    @Override
    public NodeInfo mapNodeInfo(final Map<String, Object> data) {
        return nodeInfoMapper.map(data);
    }
}
