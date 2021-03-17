package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeStats;

import java.time.Duration;

/**
 * Utility class for creating random node stats.
 */
public final class NodeStatsUtils {

    private static final int ONE_HUNDRED = 100;

    private NodeStatsUtils() {
        // instantiation protection
    }

    public static NodeStats random() {
        return new NodeStats(
                Randoms.generatePositiveInteger(),
                Randoms.generateInteger(ONE_HUNDRED),
                Duration.ofMillis(Randoms.generatePositiveInteger()),
                Randoms.generateString("uptime-"),
                Randoms.generateInteger(ONE_HUNDRED),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger()
        );
    }
}
