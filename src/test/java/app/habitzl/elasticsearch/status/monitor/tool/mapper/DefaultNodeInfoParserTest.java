package app.habitzl.elasticsearch.status.monitor.tool.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class DefaultNodeInfoParserTest {

	private static final String NODE_ROLE_MASTER_DATA_PLUS_UNKNOWN_CHARS =
			"#" + DefaultNodeInfoParser.DATA_NODE_ROLE_ID
					+ "~" + DefaultNodeInfoParser.MASTER_ELIGIBLE_NODE_ROLE_ID
					+ "ß";
	private static final String NODE_ROLE_UNKNOWN_CHARS = "~#ß";

	private static final String TEST_IP = "1.2.3.4";
	private static final Integer TEST_RAM = 75;
	private static final Integer TEST_HEAP = 33;
	private static final String TEST_NODE_NAME = "node name";
	private static final Float TEST_LOAD_AVERAGE = 1.23f;

	private DefaultNodeInfoParser sut;

	@BeforeEach
	void setUp() {
		sut = new DefaultNodeInfoParser();
	}

	@Test
	void parse_validNodeInfoData_returnsNodeInfo() {
		// Given
		Map<String, Object> map = Map.ofEntries(
				Map.entry(DefaultNodeInfoParser.IP_KEY, TEST_IP),
				Map.entry(DefaultNodeInfoParser.RAM_PERCENT_KEY, TEST_RAM.toString()),
				Map.entry(DefaultNodeInfoParser.HEAP_PERCENT_KEY, TEST_HEAP.toString()),
				Map.entry(DefaultNodeInfoParser.NODE_NAME_KEY, TEST_NODE_NAME),
				Map.entry(DefaultNodeInfoParser.NODE_MASTER_KEY, DefaultNodeInfoParser.MASTER_NODE_MARKER),
				Map.entry(DefaultNodeInfoParser.NODE_ROLE_KEY, NODE_ROLE_MASTER_DATA_PLUS_UNKNOWN_CHARS),
				Map.entry(DefaultNodeInfoParser.AVERAGE_LOAD_KEY, TEST_LOAD_AVERAGE.toString())
		);

		// When
		NodeInfo result = sut.parse(map);

		// Then
		EndpointInfo expectedEndpointInfo = new EndpointInfo(TEST_IP, TEST_RAM, TEST_HEAP);
		NodeInfo expectedNodeInfo = new NodeInfo(
				TEST_NODE_NAME,
				true,
				true,
				true,
				TEST_LOAD_AVERAGE,
				expectedEndpointInfo
		);
		assertThat(result, equalTo(expectedNodeInfo));
	}

	@Test
	void parse_notMasterEligibleOrDataNode_returnsNodeInfo() {
		// Given
		Map<String, Object> map = Map.ofEntries(
				Map.entry(DefaultNodeInfoParser.NODE_ROLE_KEY, NODE_ROLE_UNKNOWN_CHARS)
		);

		// When
		NodeInfo result = sut.parse(map);

		// Then
		assertThat(result.isMasterEligibleNode(), is(false));
		assertThat(result.isDataNode(), is(false));
	}

	@Test
	void parse_notCurrentMasterNode_returnsNodeInfo() {
		// Given
		Map<String, Object> map = Map.ofEntries(
				Map.entry(DefaultNodeInfoParser.NODE_MASTER_KEY, "-")
		);

		// When
		NodeInfo result = sut.parse(map);

		// Then
		assertThat(result.isMasterNode(), is(false));
	}
}