package app.habitzl.elasticsearch.status.monitor.tool.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DefaultInfoParserTest {

	private static final Map<String, Object> TEST_MAP = Map.of("key", "value");

	private DefaultInfoParser sut;
	private NodeInfoParser nodeInfoParser;

	@BeforeEach
	void setUp() {
		nodeInfoParser = mock(NodeInfoParser.class);
		sut = new DefaultInfoParser(nodeInfoParser);
	}

	@Test
	void parseNodeInfo_sut_delegatesToNodeInfoParser() {
		// Given
		Map<String, Object> map = TEST_MAP;

		// When
		sut.parseNodeInfo(map);

		// Then
		verify(nodeInfoParser).parse(map);
	}
}