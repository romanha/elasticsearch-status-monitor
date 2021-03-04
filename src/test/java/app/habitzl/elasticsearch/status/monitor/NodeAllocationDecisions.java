package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.NodeAllocationDecision;

import java.util.List;

/**
 * Utility class for creating random unassigned shard infos.
 */
public final class NodeAllocationDecisions {

    private NodeAllocationDecisions() {
        // instantiation protection
    }

    public static NodeAllocationDecision random() {
        return new NodeAllocationDecision(
                Randoms.generateString("node-id-"),
                Randoms.generateString("node-name-"),
                List.of(randomDecision(), randomDecision())
        );
    }

    private static String randomDecision() {
        return Randoms.generateString("node does not match setting ");
    }
}
