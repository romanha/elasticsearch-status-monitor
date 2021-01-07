package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

import java.security.SecureRandom;
import java.time.Duration;

/**
 * Utility class for creating random node infos.
 */
public final class NodeInfos {

    private NodeInfos() {
        // instantiation protection
    }

    public static NodeInfo randomNode() {
        return randomMasterEligibleDataNode(EndpointInfos.random());
    }

    public static NodeInfo randomMasterEligibleDataNode() {
        return random(EndpointInfos.random(), true, true);
    }

    public static NodeInfo randomMasterEligibleDataNode(final EndpointInfo endpoint) {
        return random(endpoint, true, true);
    }

    public static NodeInfo randomMasterEligibleNode(final EndpointInfo endpoint, final boolean isDataNode) {
        return random(endpoint, true, isDataNode);
    }

    public static NodeInfo randomNotMasterEligibleDataNode() {
        return random(EndpointInfos.random(), false, true);
    }

    private static NodeInfo random(final EndpointInfo endpoint, final boolean isMasterEligible, final boolean isDataNode) {
        SecureRandom random = new SecureRandom();
        return new NodeInfo(
                "P" + random.nextInt(),
                "node-id-" + random.nextInt(),
                "node-name-" + random.nextInt(),
                random.nextBoolean(),
                isDataNode,
                isMasterEligible,
                Duration.ofMillis(random.nextInt()).toString(),
                random.nextFloat(),
                endpoint
        );
    }
}
