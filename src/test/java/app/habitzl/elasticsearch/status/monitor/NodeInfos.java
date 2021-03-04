package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

import java.time.Duration;

/**
 * Utility class for creating random node infos.
 */
public final class NodeInfos {

    private NodeInfos() {
        // instantiation protection
    }

    public static NodeInfo random() {
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
        return new NodeInfo(
                Randoms.generateString("P"),
                Randoms.generateString("node-id-"),
                Randoms.generateString("node-name-"),
                Randoms.generateBoolean(),
                isDataNode,
                isMasterEligible,
                Duration.ofMillis(Randoms.generatePositiveInteger()).toString(),
                Randoms.generateFloat(),
                endpoint
        );
    }
}
