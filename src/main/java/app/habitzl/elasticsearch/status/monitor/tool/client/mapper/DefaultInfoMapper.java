package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.InfoMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;

import javax.inject.Inject;
import java.util.List;

public class DefaultInfoMapper implements InfoMapper {

    private final ClusterSettingsMapper clusterSettingsMapper;
    private final ClusterInfoMapper clusterInfoMapper;
    private final NodeInfoMapper nodeInfoMapper;
    private final ClusterAllocationMapper clusterAllocationMapper;

    @Inject
    public DefaultInfoMapper(
            final ClusterSettingsMapper clusterSettingsMapper,
            final ClusterInfoMapper clusterInfoMapper,
            final NodeInfoMapper nodeInfoMapper,
            final ClusterAllocationMapper clusterAllocationMapper) {
        this.clusterSettingsMapper = clusterSettingsMapper;
        this.clusterInfoMapper = clusterInfoMapper;
        this.nodeInfoMapper = nodeInfoMapper;
        this.clusterAllocationMapper = clusterAllocationMapper;
    }

    @Override
    public ClusterSettings mapClusterSettings(final String jsonData) {
        return clusterSettingsMapper.map(jsonData);
    }

    @Override
    public ClusterInfo mapClusterInfo(final String clusterHealthJson, final String clusterStateJson) {
        return clusterInfoMapper.map(clusterHealthJson, clusterStateJson);
    }

    @Override
    public List<NodeInfo> mapNodeInfo(final String nodeInfoJson, final String nodeStatsJson) {
        return nodeInfoMapper.map(nodeInfoJson, nodeStatsJson);
    }

    @Override
    public UnassignedShardInfo mapUnassignedShardInfo(final String jsonData) {
        return clusterAllocationMapper.map(jsonData);
    }
}
