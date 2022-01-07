package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.Randoms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DefaultInfoMapperTest {

    private DefaultInfoMapper sut;
    private ClusterSettingsMapper clusterSettingsMapper;
    private ClusterInfoMapper clusterInfoMapper;
    private NodeInfoMapper nodeInfoMapper;
    private ClusterAllocationMapper clusterAllocationMapper;

    @BeforeEach
    void setUp() {
        clusterSettingsMapper = mock(ClusterSettingsMapper.class);
        clusterInfoMapper = mock(ClusterInfoMapper.class);
        nodeInfoMapper = mock(NodeInfoMapper.class);
        clusterAllocationMapper = mock(ClusterAllocationMapper.class);
        sut = new DefaultInfoMapper(
                clusterSettingsMapper,
                clusterInfoMapper,
                nodeInfoMapper,
                clusterAllocationMapper
        );
    }

    @Test
    void mapClusterSettings_sut_delegatesToClusterSettingsMapper() {
        // Given
        String json = Randoms.generateString("cluster-settings-");

        // When
        sut.mapClusterSettings(json);

        // Then
        verify(clusterSettingsMapper).map(json);
    }

    @Test
    void mapClusterInfo_sut_delegatesToClusterInfoMapper() {
        // Given
        String json1 = Randoms.generateString("cluster-health-");
        String json2 = Randoms.generateString("cluster-state-");
        String json3 = Randoms.generateString("cluster-stats-");

        // When
        sut.mapClusterInfo(json1, json2, json3);

        // Then
        verify(clusterInfoMapper).map(json1, json2, json3);
    }

    @Test
    void mapNodeInfo_sut_delegatesToNodeInfoMapper() {
        // Given
        String masterNodeJson = Randoms.generateString("master-node-");
        String nodeInfoJson = Randoms.generateString("node-info-");
        String nodeStatsJson = Randoms.generateString("node-stats-");

        // When
        sut.mapNodeInfo(masterNodeJson, nodeInfoJson, nodeStatsJson);

        // Then
        verify(nodeInfoMapper).map(masterNodeJson, nodeInfoJson, nodeStatsJson);
    }

    @Test
    void mapUnassignedShardInfo_sut_delegatesToClusterAllocationMapper() {
        // Given
        String json = Randoms.generateString("unassigned-shards-");

        // When
        sut.mapUnassignedShardInfo(json);

        // Then
        verify(clusterAllocationMapper).map(json);
    }
}