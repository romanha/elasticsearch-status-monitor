package app.habitzl.elasticsearch.status.monitor.tool.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.params.NodeParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultNodeInfoParserTest {

	private static final String NODE_ROLE_MASTER_DATA_PLUS_UNKNOWN_CHARS =
			"#" + DefaultNodeInfoParser.DATA_NODE_ROLE_ID
					+ "~" + DefaultNodeInfoParser.MASTER_ELIGIBLE_NODE_ROLE_ID
					+ "ß";
	private static final String NODE_ROLE_UNKNOWN_CHARS = "~#ß";

	private static final String TEST_IP = "1.2.3.4";
	private static final Integer TEST_RAM = 75;
	private static final Integer TEST_HEAP = 33;
	private static final String TEST_PROCESS_ID = "1234";
	private static final String TEST_NODE_ID = "k0zy";
	private static final String TEST_NODE_NAME = "node name";
	private static final String TEST_UPTIME_IN_SECONDS = "300";
	private static final Duration TEST_UPTIME_DURATION = Duration.ofSeconds(300);
	private static final String TEST_UPTIME_DURATION_FORMATTED = "0:00:05";
	private static final Float TEST_LOAD_AVERAGE = 1.23f;

	private DefaultNodeInfoParser sut;
	private TimeParser timeParser;
	private TimeFormatter timeFormatter;

	@BeforeEach
	void setUp() {
		timeParser = mock(TimeParser.class);
		timeFormatter = mock(TimeFormatter.class);
		sut = new DefaultNodeInfoParser(timeParser, timeFormatter);
	}

	@Test
	void parse_validNodeInfoData_returnsNodeInfo() {
		// Given
		when(timeParser.parse(TEST_UPTIME_IN_SECONDS)).thenReturn(TEST_UPTIME_DURATION);
		when(timeFormatter.format(TEST_UPTIME_DURATION)).thenReturn(TEST_UPTIME_DURATION_FORMATTED);
		Map<String, Object> map = Map.ofEntries(
				Map.entry(NodeParams.IP_KEY, TEST_IP),
				Map.entry(NodeParams.RAM_PERCENT_KEY, TEST_RAM.toString()),
				Map.entry(NodeParams.HEAP_PERCENT_KEY, TEST_HEAP.toString()),
				Map.entry(NodeParams.NODE_PROCESS_ID, TEST_PROCESS_ID),
				Map.entry(NodeParams.NODE_ID_KEY, TEST_NODE_ID),
				Map.entry(NodeParams.NODE_NAME_KEY, TEST_NODE_NAME),
				Map.entry(NodeParams.NODE_MASTER_KEY, DefaultNodeInfoParser.MASTER_NODE_MARKER),
				Map.entry(NodeParams.NODE_ROLE_KEY, NODE_ROLE_MASTER_DATA_PLUS_UNKNOWN_CHARS),
				Map.entry(NodeParams.NODE_UPTIME, TEST_UPTIME_IN_SECONDS),
				Map.entry(NodeParams.AVERAGE_LOAD_KEY, TEST_LOAD_AVERAGE.toString())
		);

		// When
		NodeInfo result = sut.parse(map);

		// Then
		EndpointInfo expectedEndpointInfo = new EndpointInfo(TEST_IP, TEST_RAM, TEST_HEAP);
		NodeInfo expectedNodeInfo = new NodeInfo(
				TEST_PROCESS_ID,
				TEST_NODE_ID,
				TEST_NODE_NAME,
				true,
				true,
				true,
				TEST_UPTIME_DURATION_FORMATTED,
				TEST_LOAD_AVERAGE,
				expectedEndpointInfo
		);
		assertThat(result, equalTo(expectedNodeInfo));
	}

	@Test
	void parse_notMasterEligibleOrDataNode_returnsNodeInfo() {
		// Given
		Map<String, Object> map = Map.ofEntries(
				Map.entry(NodeParams.NODE_ROLE_KEY, NODE_ROLE_UNKNOWN_CHARS)
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
				Map.entry(NodeParams.NODE_MASTER_KEY, "-")
		);

		// When
		NodeInfo result = sut.parse(map);

		// Then
		assertThat(result.isMasterNode(), is(false));
	}
}