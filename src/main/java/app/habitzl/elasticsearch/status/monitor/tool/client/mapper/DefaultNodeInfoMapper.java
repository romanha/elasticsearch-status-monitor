package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeStats;
import app.habitzl.elasticsearch.status.monitor.util.TimeFormatter;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultNodeInfoMapper implements NodeInfoMapper {
    private static final Logger LOG = LogManager.getLogger(DefaultNodeInfoMapper.class);

    // Master node path
    private static final String PATH_MASTER_NODE = "master_node";

    // Node info paths
    private static final String PATH_NODE_NAME = "name";
    private static final String PATH_PROCESS_ID = "process.id";
    private static final String PATH_JVM_VERSION = "jvm.version";
    private static final String PATH_ES_VERSION = "version";
    private static final String PATH_ROLES = "roles";

    // Endpoint info paths
    private static final String PATH_IP_ADDRESS = "ip";
    private static final String PATH_HTTP_PUBLISH_ADDRESS = "http.publish_address";
    private static final String PATH_OS_NAME = "os.pretty_name";
    private static final String PATH_OS_PROCESSORS = "os.available_processors";
    private static final String PATH_OS_CPU_PERCENT = "os.cpu.percent";
    private static final String PATH_OS_CPU_LOAD_AVERAGE = "os.cpu.load_average.15m";
    private static final String PATH_RAM_PERCENT = "os.mem.used_percent";
    private static final String PATH_RAM_BYTES = "os.mem.used_in_bytes";

    // Node stats paths
    private static final String PATH_FS_AVAILABLE_BYTES = "fs.total.available_in_bytes";
    private static final String PATH_PROCESS_CPU_PERCENT = "process.cpu.percent";
    private static final String PATH_UPTIME = "jvm.uptime_in_millis";
    private static final String PATH_PROCESS_HEAP_PERCENT = "jvm.mem.heap_used_percent";
    private static final String PATH_PROCESS_HEAP_BYTES = "jvm.mem.heap_used_in_bytes";
    private static final String PATH_PROCESS_MAX_HEAP_BYTES = "jvm.mem.heap_max_in_bytes";

    private static final String ROLE_MASTER_ELIGIBLE = "master";
    private static final String ROLE_DATA = "data";
    private static final String ROLE_INGEST = "ingest";

    private final TimeFormatter timeFormatter;

    @Inject
    public DefaultNodeInfoMapper(final TimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    @Override
    public List<NodeInfo> map(final String masterNodeJson, final String nodeInfoJson, final String nodeStatsJson) {
        DocumentContext masterNodeContext = JsonPath.parse(masterNodeJson);
        DocumentContext nodeInfoContext = JsonPath.parse(nodeInfoJson);
        DocumentContext nodeStatsContext = JsonPath.parse(nodeStatsJson);

        String masterNodeId = getMasterNodeId(masterNodeContext);
        Set<String> nodeIds = getNodeIds(nodeInfoContext);

        return nodeIds.stream()
                .map(nodeId -> gatherNodeInfo(nodeId, masterNodeId, nodeInfoContext, nodeStatsContext))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private String getMasterNodeId(final DocumentContext masterNodeContext) {
        return masterNodeContext.read(PATH_MASTER_NODE, String.class);
    }

    @SuppressWarnings("unchecked")
    private Set<String> getNodeIds(final DocumentContext context) {
        Set<String> nodeIds;
        try {
            nodeIds = context.read("$.nodes.keys()", Set.class);
        } catch (final PathNotFoundException exception) {
            nodeIds = Set.of();
            LOG.error("Could not parse any node IDs from the Elasticsearch node API.", exception);
        }

        return nodeIds;
    }

    private Optional<NodeInfo> gatherNodeInfo(
            final String nodeId,
            final String masterNodeId,
            final DocumentContext nodeInfoContext,
            final DocumentContext nodeStatsContext) {
        NodeInfo nodeInfo;

        try {
            EndpointInfo endpointInfo = gatherEndpointInfo(nodeId, nodeInfoContext, nodeStatsContext);
            NodeStats nodeStats = gatherNodeStats(nodeId, nodeStatsContext);

            String nodeName = nodeInfoContext.read(createPathForNode(nodeId, PATH_NODE_NAME), String.class);
            Integer processId = nodeInfoContext.read(createPathForNode(nodeId, PATH_PROCESS_ID), Integer.class);
            String jvmVersion = nodeInfoContext.read(createPathForNode(nodeId, PATH_JVM_VERSION), String.class);
            String elasticsearchVersion = nodeInfoContext.read(createPathForNode(nodeId, PATH_ES_VERSION), String.class);
            List<String> roles = getNodeRoles(nodeId, nodeInfoContext);

            nodeInfo = new NodeInfo(
                    nodeId,
                    nodeName,
                    Integer.toString(processId),
                    jvmVersion,
                    elasticsearchVersion,
                    Objects.equals(nodeId, masterNodeId),
                    roles.contains(ROLE_MASTER_ELIGIBLE),
                    roles.contains(ROLE_DATA),
                    roles.contains(ROLE_INGEST),
                    endpointInfo,
                    nodeStats
            );
        } catch (final PathNotFoundException | ClassCastException exception) {
            nodeInfo = null;
            LOG.error("Failed to parse node info!", exception);
        }

        return Optional.ofNullable(nodeInfo);
    }

    private EndpointInfo gatherEndpointInfo(final String nodeId, final DocumentContext nodeInfoContext, final DocumentContext nodeStatsContext) {
        String ipAddress = nodeInfoContext.read(createPathForNode(nodeId, PATH_IP_ADDRESS), String.class);
        String httpPublishAddress = nodeInfoContext.read(createPathForNode(nodeId, PATH_HTTP_PUBLISH_ADDRESS), String.class);
        String osName = nodeInfoContext.read(createPathForNode(nodeId, PATH_OS_NAME), String.class);
        Integer availableProcessors = nodeInfoContext.read(createPathForNode(nodeId, PATH_OS_PROCESSORS), Integer.class);
        Integer cpuUsageInPercent = nodeStatsContext.read(createPathForNode(nodeId, PATH_OS_CPU_PERCENT), Integer.class);
        Float cpuLoadAverage;
        Integer ramUsageInPercent = nodeStatsContext.read(createPathForNode(nodeId, PATH_RAM_PERCENT), Integer.class);
        Long ramUsageInBytes = nodeStatsContext.read(createPathForNode(nodeId, PATH_RAM_BYTES), Long.class);

        try {
            cpuLoadAverage = nodeStatsContext.read(createPathForNode(nodeId, PATH_OS_CPU_LOAD_AVERAGE), Float.class);
        } catch (final PathNotFoundException exception) {
            cpuLoadAverage = null;
            LOG.debug("The Elasticsearch node stats API did not return any data for the CPU load average.");
        }

        return new EndpointInfo(
                ipAddress,
                httpPublishAddress,
                osName,
                availableProcessors,
                cpuUsageInPercent,
                cpuLoadAverage,
                ramUsageInPercent,
                ramUsageInBytes
        );
    }

    private NodeStats gatherNodeStats(final String nodeId, final DocumentContext nodeStatsContext) {
        Long fileSystemAvailableBytes = nodeStatsContext.read(createPathForNode(nodeId, PATH_FS_AVAILABLE_BYTES), Long.class);
        Integer cpuUsageInPercent = nodeStatsContext.read(createPathForNode(nodeId, PATH_PROCESS_CPU_PERCENT), Integer.class);
        Long uptimeInMillis = nodeStatsContext.read(createPathForNode(nodeId, PATH_UPTIME), Long.class);
        Integer heapUsageInPercent = nodeStatsContext.read(createPathForNode(nodeId, PATH_PROCESS_HEAP_PERCENT), Integer.class);
        Long heapUsageInBytes = nodeStatsContext.read(createPathForNode(nodeId, PATH_PROCESS_HEAP_BYTES), Long.class);
        Long maxHeapInBytes = nodeStatsContext.read(createPathForNode(nodeId, PATH_PROCESS_MAX_HEAP_BYTES), Long.class);

        Duration uptime = Duration.ofMillis(uptimeInMillis);
        return new NodeStats(
                fileSystemAvailableBytes,
                cpuUsageInPercent,
                uptime,
                timeFormatter.format(uptime),
                heapUsageInPercent,
                heapUsageInBytes,
                maxHeapInBytes
        );
    }

    @SuppressWarnings("unchecked")
    private List<String> getNodeRoles(final String nodeId, final DocumentContext nodeInfoContext) {
        return nodeInfoContext.read(createPathForNode(nodeId, PATH_ROLES), List.class);
    }

    private String createPathForNode(final String nodeId, final String path) {
        return "$.nodes." + nodeId + "." + path;
    }
}
