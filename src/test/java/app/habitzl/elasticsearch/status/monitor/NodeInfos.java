package app.habitzl.elasticsearch.status.monitor;

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
        SecureRandom random = new SecureRandom();

        return new NodeInfo(
                "P" + random.nextInt(),
                "node-id-" + random.nextInt(),
                "node-name-" + random.nextInt(),
                random.nextBoolean(),
                random.nextBoolean(),
                random.nextBoolean(),
                Duration.ofMillis(random.nextInt()).toString(),
                random.nextFloat(),
                EndpointInfos.random()
        );
    }
}
