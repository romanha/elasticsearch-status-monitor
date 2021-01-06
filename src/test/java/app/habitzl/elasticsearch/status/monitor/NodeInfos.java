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

    public static NodeInfo random() {
        return randomMasterEligible(EndpointInfos.random());
    }

    public static NodeInfo randomMasterEligible(final EndpointInfo endpoint) {
        SecureRandom random = new SecureRandom();
        return new NodeInfo(
                "P" + random.nextInt(),
                "node-id-" + random.nextInt(),
                "node-name-" + random.nextInt(),
                random.nextBoolean(),
                random.nextBoolean(),
                true,
                Duration.ofMillis(random.nextInt()).toString(),
                random.nextFloat(),
                endpoint
        );
    }

    public static NodeInfo randomMasterEligible() {
        return random(true);
    }

    public static NodeInfo randomNotMasterEligible() {
        return random(false);
    }

    private static NodeInfo random(final boolean isMasterEligible) {
        SecureRandom random = new SecureRandom();
        return new NodeInfo(
                "P" + random.nextInt(),
                "node-id-" + random.nextInt(),
                "node-name-" + random.nextInt(),
                random.nextBoolean(),
                random.nextBoolean(),
                isMasterEligible,
                Duration.ofMillis(random.nextInt()).toString(),
                random.nextFloat(),
                EndpointInfos.random()
        );
    }
}
