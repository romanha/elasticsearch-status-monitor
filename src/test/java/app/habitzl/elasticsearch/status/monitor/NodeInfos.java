package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

/**
 * Utility class for creating random node infos.
 */
public final class NodeInfos {

    private static final int ONE_HUNDRED = 100;

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
                Randoms.generateString("node-id-"),
                Randoms.generateString("node-name-"),
                Randoms.generateString("P"),
                Randoms.generateString("jvm-version-"),
                Randoms.generateString("elasticsearch-version-"),
                Randoms.generateBoolean(),
                isMasterEligible,
                isDataNode,
                Randoms.generateBoolean(),
                endpoint,
                NodeStatsUtils.random()
        );
    }
}
