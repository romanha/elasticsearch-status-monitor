package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.CatNodesParams;
import app.habitzl.elasticsearch.status.monitor.util.TimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultNodeInfoMapperTest {

    private static final String NODE_ROLE_MASTER_DATA_PLUS_UNKNOWN_CHARS =
            "#" + DefaultNodeInfoMapper.DATA_NODE_ROLE_ID
                    + "~" + DefaultNodeInfoMapper.MASTER_ELIGIBLE_NODE_ROLE_ID
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

    private DefaultNodeInfoMapper sut;
    private TimeParser timeParser;
    private TimeFormatter timeFormatter;

    @BeforeEach
    void setUp() {
        timeParser = mock(TimeParser.class);
        timeFormatter = mock(TimeFormatter.class);
        sut = new DefaultNodeInfoMapper(timeParser, timeFormatter);
    }

    @Test
    void map_validNodeInfoData_returnsNodeInfo() {
        // Given
        when(timeParser.parse(TEST_UPTIME_IN_SECONDS)).thenReturn(TEST_UPTIME_DURATION);
        when(timeFormatter.format(TEST_UPTIME_DURATION)).thenReturn(TEST_UPTIME_DURATION_FORMATTED);
        Map<String, Object> map = Map.ofEntries(
                Map.entry(CatNodesParams.IP_COLUMN, TEST_IP),
                Map.entry(CatNodesParams.RAM_PERCENT_COLUMN, TEST_RAM.toString()),
                Map.entry(CatNodesParams.HEAP_PERCENT_COLUMN, TEST_HEAP.toString()),
                Map.entry(CatNodesParams.NODE_PROCESS_ID_COLUMN, TEST_PROCESS_ID),
                Map.entry(CatNodesParams.NODE_ID_COLUMN, TEST_NODE_ID),
                Map.entry(CatNodesParams.NODE_NAME_COLUMN, TEST_NODE_NAME),
                Map.entry(CatNodesParams.NODE_MASTER_COLUMN, DefaultNodeInfoMapper.MASTER_NODE_MARKER),
                Map.entry(CatNodesParams.NODE_ROLE_COLUMN, NODE_ROLE_MASTER_DATA_PLUS_UNKNOWN_CHARS),
                Map.entry(CatNodesParams.NODE_UPTIME_COLUMN, TEST_UPTIME_IN_SECONDS),
                Map.entry(CatNodesParams.AVERAGE_LOAD_COLUMN, TEST_LOAD_AVERAGE.toString())
        );

        // When
        NodeInfo result = sut.map(map);

        // Then
        // TODO
        EndpointInfo expectedEndpointInfo = null;
//        new EndpointInfo(TEST_IP, operatingSystemName, availableProcessors, cpuLoadAverageLast15Minutes, TEST_RAM, ramUsageInBytes);
        NodeInfo expectedNodeInfo = null;
//                new NodeInfo(
//                TEST_PROCESS_ID,
//                TEST_NODE_ID,
//                TEST_NODE_NAME,
//                jvmVersion,
//                elasticsearchVersion,
//                true,
//                true,
//                true,
//                TEST_UPTIME_DURATION_FORMATTED,
//                TEST_LOAD_AVERAGE,
//                TEST_HEAP,
//                isIngestNode,
//                expectedEndpointInfo,
//                null
//        );
        assertThat(result, equalTo(expectedNodeInfo));
    }

    @Test
    void map_notMasterEligibleOrDataNode_returnsNodeInfo() {
        // Given
        Map<String, Object> map = Map.ofEntries(
                Map.entry(CatNodesParams.NODE_ROLE_COLUMN, NODE_ROLE_UNKNOWN_CHARS)
        );

        // When
        NodeInfo result = sut.map(map);

        // Then
        assertThat(result.isMasterEligibleNode(), is(false));
        assertThat(result.isDataNode(), is(false));
    }

    @Test
    void map_notCurrentMasterNode_returnsNodeInfo() {
        // Given
        Map<String, Object> map = Map.ofEntries(
                Map.entry(CatNodesParams.NODE_MASTER_COLUMN, "-")
        );

        // When
        NodeInfo result = sut.map(map);

        // Then
        assertThat(result.isMasterNode(), is(false));
    }
}