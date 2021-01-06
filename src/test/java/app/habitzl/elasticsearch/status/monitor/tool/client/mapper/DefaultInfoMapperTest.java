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
    private NodeInfoMapper nodeInfoMapper;

    @BeforeEach
    void setUp() {
        clusterSettingsMapper = mock(ClusterSettingsMapper.class);
        nodeInfoMapper = mock(NodeInfoMapper.class);
        sut = new DefaultInfoMapper(clusterSettingsMapper, nodeInfoMapper);
    }

    @Test
    void mapClusterSettings_sut_delegatesToClusterSettingsMapper() {
        // Given
        // test string

        // When
        sut.mapClusterSettings(TEST_STRING);

        // Then
        verify(clusterSettingsMapper).map(TEST_STRING);
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
}