package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DefaultInfoMapperTest {

    private static final String TEST_STRING = "value";
    private static final Map<String, Object> TEST_MAP = Map.of("key", "value");

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
        String json = TEST_STRING;

        // When
        sut.mapClusterSettings(json);

        // Then
        verify(clusterSettingsMapper).map(json);
    }

    @Test
    void mapClusterInfo_sut_delegatesToClusterInfoMapper() {
        // Given
        String json = TEST_STRING;

        // When
        sut.mapClusterInfo(json);

        // Then
        verify(clusterInfoMapper).map(json);
    }

    @Test
    void mapNodeInfo_sut_delegatesToNodeInfoMapper() {
        // Given
        Map<String, Object> map = TEST_MAP;

        // When
        sut.mapNodeInfo(map);

        // Then
        verify(nodeInfoMapper).map(map);
    }

    @Test
    void mapUnassignedShardInfo_sut_delegatesToClusterAllocationMapper() {
        // Given
        String json = TEST_STRING;

        // When
        sut.mapUnassignedShardInfo(json);

        // Then
        verify(clusterAllocationMapper).map(json);
    }
}