package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.NodeAllocationDecision;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * Utility class for creating random unassigned shard infos.
 */
public final class NodeAllocationDecisions {

    private NodeAllocationDecisions() {
        // instantiation protection
    }

    public static NodeAllocationDecision random() {
        SecureRandom random = new SecureRandom();

        return new NodeAllocationDecision(
                "node-id-" + random.nextInt(),
                "node-name-" + random.nextInt(),
                List.of(randomDecision(random), randomDecision(random))
        );
    }

    private static String randomDecision(final Random random) {
        return "node does not match setting " + random.nextInt();
    }
}
