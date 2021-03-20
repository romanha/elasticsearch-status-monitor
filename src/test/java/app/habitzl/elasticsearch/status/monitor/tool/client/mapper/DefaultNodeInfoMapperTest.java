package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeStats;
import app.habitzl.elasticsearch.status.monitor.util.TimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultNodeInfoMapperTest {

    private static final NodeInfo NODE_INFO_1 = NodeInfos.randomMasterEligibleDataNode();
    private static final NodeInfo NODE_INFO_2 = NodeInfos.randomNotMasterEligibleDataNode();

    private static String createNodeInfoResponse(final NodeInfo... nodeInfos) {
        String jsonNodeInfos = Arrays.stream(nodeInfos)
                                     .map(DefaultNodeInfoMapperTest::createNodeInfoJson)
                                     .collect(Collectors.joining(",\n", "", "\n"));
        return "{\n"
                + "  \"_nodes\": {\n"
                + "    \"total\": " + nodeInfos.length + ",\n"
                + "    \"successful\": " + nodeInfos.length + ",\n"
                + "    \"failed\": 0\n"
                + "  },\n"
                + "  \"cluster_name\": \"local-cluster\",\n"
                + "  \"nodes\": {\n"
                + jsonNodeInfos
                + "  }\n"
                + "}";
    }

    private static String createNodeInfoJson(final NodeInfo nodeInfo) {
        List<String> nodeRoles = new ArrayList<>();
        if (nodeInfo.isMasterEligibleNode()) {
            nodeRoles.add("\"master\"");
        }

        if (nodeInfo.isDataNode()) {
            nodeRoles.add("\"data\"");
        }

        if (nodeInfo.isIngestNode()) {
            nodeRoles.add("\"ingest\"");
        }

        EndpointInfo endpointInfo = nodeInfo.getEndpointInfo();
        return "    \"" + nodeInfo.getNodeId() + "\": {\n"
                + "      \"name\": \"" + nodeInfo.getNodeName() + "\",\n"
                + "      \"transport_address\": \"127.0.0.1:9300\",\n"
                + "      \"host\": \"" + endpointInfo.getIpAddress() + "\",\n"
                + "      \"ip\": \"" + endpointInfo.getIpAddress() + "\",\n"
                + "      \"version\": \"" + nodeInfo.getElasticsearchVersion() + "\",\n"
                + "      \"build_flavor\": \"oss\",\n"
                + "      \"build_type\": \"zip\",\n"
                + "      \"build_hash\": \"be13c69\",\n"
                + "      \"roles\": [" + String.join(",", nodeRoles) + "],\n"
                + "      \"os\": {\n"
                + "        \"refresh_interval_in_millis\": 1000,\n"
                + "        \"name\": \"" + endpointInfo.getOperatingSystemName() + "\",\n"
                + "        \"pretty_name\": \"" + endpointInfo.getOperatingSystemName() + "\",\n"
                + "        \"arch\": \"amd64\",\n"
                + "        \"version\": \"10.0\",\n"
                + "        \"available_processors\": " + endpointInfo.getAvailableProcessors() + ",\n"
                + "        \"allocated_processors\": " + endpointInfo.getAvailableProcessors() + "\n"
                + "      },\n"
                + "      \"process\": {\n"
                + "        \"refresh_interval_in_millis\": 1000,\n"
                + "        \"id\": " + nodeInfo.getProcessId() + "\n"
                + "      },\n"
                + "      \"jvm\": {\n"
                + "        \"pid\": " + nodeInfo.getProcessId() + ",\n"
                + "        \"version\": \"" + nodeInfo.getJvmVersion() + "\",\n"
                + "        \"vm_name\": \"OpenJDK 64-Bit Server VM\",\n"
                + "        \"vm_version\": \"11.0.9.1+1-LTS\",\n"
                + "        \"vm_vendor\": \"Azul Systems, Inc.\",\n"
                + "        \"start_time_in_millis\": 1616251375051,\n"
                + "        \"mem\": {\n"
                + "          \"heap_init_in_bytes\": 524288000,\n"
                + "          \"heap_max_in_bytes\": 506855424,\n"
                + "          \"non_heap_init_in_bytes\": 7667712,\n"
                + "          \"non_heap_max_in_bytes\": 0,\n"
                + "          \"direct_max_in_bytes\": 0\n"
                + "        }\n"
                + "      }\n"
                + "    }";
    }

    private static String createNodeStatsResponse(final NodeInfo... nodeInfos) {
        String jsonNodeStats = Arrays.stream(nodeInfos)
                                     .map(DefaultNodeInfoMapperTest::createNodeStatsJson)
                                     .collect(Collectors.joining(",\n", "", "\n"));
        return "{\n"
                + "  \"_nodes\": {\n"
                + "    \"total\": " + nodeInfos.length + ",\n"
                + "    \"successful\": " + nodeInfos.length + ",\n"
                + "    \"failed\": 0\n"
                + "  },\n"
                + "  \"cluster_name\": \"local-cluster\",\n"
                + "  \"nodes\": {\n"
                + jsonNodeStats
                + "  }\n"
                + "}";
    }

    private static String createNodeStatsJson(final NodeInfo nodeInfo) {
        EndpointInfo endpointInfo = nodeInfo.getEndpointInfo();
        NodeStats nodeStats = nodeInfo.getNodeStats();
        return "    \"" + nodeInfo.getNodeId() + "\": {\n"
                + "      \"timestamp\": 1616253436336,\n"
                + "      \"name\": \"" + nodeInfo.getNodeName() + "\",\n"
                + "      \"transport_address\": \"127.0.0.1:9300\",\n"
                + "      \"host\": \"" + endpointInfo.getIpAddress() + "\",\n"
                + "      \"ip\": \"" + endpointInfo.getIpAddress() + "\",\n"
                + "      \"os\": {\n"
                + "        \"timestamp\": 1616253436367,\n"
                + "        \"cpu\": {\n"
                + "          \"percent\":" + endpointInfo.getCpuUsageInPercent() + ",\n"
                + "          \"load_average\": {\n"
                + "            \"15m\": " + endpointInfo.getCpuLoadAverageLast15Minutes() + "\n"
                + "          }\n"
                + "        },\n"
                + "        \"mem\": {\n"
                + "          \"total_in_bytes\": 8481501184,\n"
                + "          \"free_in_bytes\": 2699386880,\n"
                + "          \"used_in_bytes\": " + endpointInfo.getRamUsageInBytes() + ",\n"
                + "          \"free_percent\": " + (100 - endpointInfo.getRamUsageInPercent()) + ",\n"
                + "          \"used_percent\": " + endpointInfo.getRamUsageInPercent() + "\n"
                + "        }\n"
                + "      },\n"
                + "      \"process\": {\n"
                + "        \"timestamp\": 1616253436398,\n"
                + "        \"cpu\": {\n"
                + "          \"percent\": " + nodeStats.getCpuUsageInPercent() + ",\n"
                + "          \"total_in_millis\": 36421\n"
                + "        },\n"
                + "        \"mem\": {\n"
                + "          \"total_virtual_in_bytes\": 772423680\n"
                + "        }\n"
                + "      },\n"
                + "      \"jvm\": {\n"
                + "        \"timestamp\": 1616253436398,\n"
                + "        \"uptime_in_millis\": " + nodeStats.getUptime().toMillis() + ",\n"
                + "        \"mem\": {\n"
                + "          \"heap_used_in_bytes\": " + nodeStats.getHeapUsageInBytes() + ",\n"
                + "          \"heap_used_percent\": " + nodeStats.getHeapUsageInPercent() + ",\n"
                + "          \"heap_committed_in_bytes\": 506855424,\n"
                + "          \"heap_max_in_bytes\": " + nodeStats.getMaximumHeapInBytes() + ",\n"
                + "          \"non_heap_used_in_bytes\": 99798272,\n"
                + "          \"non_heap_committed_in_bytes\": 106659840\n"
                + "        }\n"
                + "      },\n"
                + "      \"fs\": {\n"
                + "        \"timestamp\": 1616253436398,\n"
                + "        \"total\": {\n"
                + "          \"total_in_bytes\": 497432915968,\n"
                + "          \"free_in_bytes\": 467212341248,\n"
                + "          \"available_in_bytes\": " + nodeStats.getAvailableBytesOnFileSystem() + "\n"
                + "        },\n"
                + "        \"least_usage_estimate\": {\n"
                + "          \"path\": \"D:\\\\Elasticsearch\\\\data\\\\nodes\\\\0\",\n"
                + "          \"total_in_bytes\": 497432915968,\n"
                + "          \"available_in_bytes\": 467212341248,\n"
                + "          \"used_disk_percent\": 6.075306589068603\n"
                + "        },\n"
                + "        \"most_usage_estimate\": {\n"
                + "          \"path\": \"D:\\\\Elasticsearch\\\\data\\\\nodes\\\\0\",\n"
                + "          \"total_in_bytes\": 497432915968,\n"
                + "          \"available_in_bytes\": 467212341248,\n"
                + "          \"used_disk_percent\": 6.075306589068603\n"
                + "        },\n"
                + "        \"data\": [\n"
                + "          {\n"
                + "            \"path\": \"D:\\\\Elasticsearch\\\\data\\\\nodes\\\\0\",\n"
                + "            \"mount\": \"DATA (D:)\",\n"
                + "            \"type\": \"NTFS\",\n"
                + "            \"total_in_bytes\": 497432915968,\n"
                + "            \"free_in_bytes\": 467212341248,\n"
                + "            \"available_in_bytes\": 467212341248\n"
                + "          }\n"
                + "        ]\n"
                + "      }\n"
                + "    }";
    }

    private DefaultNodeInfoMapper sut;
    private TimeFormatter timeFormatter;

    @BeforeEach
    void setUp() {
        var timeParser = mock(TimeParser.class);
        timeFormatter = mock(TimeFormatter.class);
        sut = new DefaultNodeInfoMapper(timeParser, timeFormatter);
    }

    @Test
    void map_validResponses_returnNodeInfos() {
        // Given
        prepareTimeFormatter(NODE_INFO_1, NODE_INFO_2);
        String nodeInfoResponse = createNodeInfoResponse(NODE_INFO_1, NODE_INFO_2);
        String nodeStatsResponse = createNodeStatsResponse(NODE_INFO_1, NODE_INFO_2);

        // When
        List<NodeInfo> result = sut.map(nodeInfoResponse, nodeStatsResponse);

        // Then
        assertThat(result, containsInAnyOrder(NODE_INFO_1, NODE_INFO_2));
    }

    private void prepareTimeFormatter(final NodeInfo... nodeInfos) {
        Arrays.stream(nodeInfos)
              .forEach(nodeInfo ->
                      when(timeFormatter.format(nodeInfo.getNodeStats().getUptime()))
                              .thenReturn(nodeInfo.getNodeStats().getUptimeFormatted())
              );
        ;
    }
}