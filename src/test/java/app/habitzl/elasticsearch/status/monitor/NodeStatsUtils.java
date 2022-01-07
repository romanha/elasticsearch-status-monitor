package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeStats;

import java.time.Duration;

/**
 * Utility class for creating random node stats.
 */
public final class NodeStatsUtils {

    private NodeStatsUtils() {
        // instantiation protection
    }

    public static NodeStats random() {
        return new NodeStats(
                Randoms.generatePositiveInteger(),
                Randoms.generatePercentage(),
                Duration.ofMillis(Randoms.generatePositiveInteger()),
                Randoms.generateString("uptime-"),
                Randoms.generatePercentage(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger()
        );
    }
}
