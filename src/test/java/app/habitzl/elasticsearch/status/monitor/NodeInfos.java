package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

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

    public static NodeInfo randomMasterNode() {
        return random(EndpointInfos.random(), true, true, Randoms.generateBoolean());
    }

    public static NodeInfo randomMasterEligibleDataNode() {
        return random(EndpointInfos.random(), Randoms.generateBoolean(), true, true);
    }

    public static NodeInfo randomMasterEligibleDataNode(final EndpointInfo endpoint) {
        return random(endpoint, Randoms.generateBoolean(), true, true);
    }

    public static NodeInfo randomMasterEligibleNode(final EndpointInfo endpoint, final boolean isDataNode) {
        return random(endpoint, Randoms.generateBoolean(), true, isDataNode);
    }

    public static NodeInfo randomNotMasterEligibleDataNode() {
        return random(EndpointInfos.random(), false, false, true);
    }

    private static NodeInfo random(
            final EndpointInfo endpoint,
            final boolean isMaster,
            final boolean isMasterEligible,
            final boolean isDataNode) {
        return new NodeInfo(
                Randoms.generateString("node-id-"),
                Randoms.generateString("node-name-"),
                Integer.toString(Randoms.generatePositiveInteger()),
                Randoms.generateString("jvm-version-"),
                Randoms.generateString("elasticsearch-version-"),
                isMaster,
                isMasterEligible,
                isDataNode,
                Randoms.generateBoolean(),
                endpoint,
                NodeStatsUtils.random()
        );
    }
}
