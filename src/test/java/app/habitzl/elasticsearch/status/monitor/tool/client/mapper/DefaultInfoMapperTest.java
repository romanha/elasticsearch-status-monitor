package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DefaultInfoMapperTest {

	private static final Map<String, Object> TEST_MAP = Map.of("key", "value");

	private DefaultInfoMapper sut;
	private NodeInfoMapper nodeInfoMapper;

	@BeforeEach
	void setUp() {
		nodeInfoMapper = mock(NodeInfoMapper.class);
		sut = new DefaultInfoMapper(nodeInfoMapper);
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