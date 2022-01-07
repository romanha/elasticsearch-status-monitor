package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterStats;

/**
 * Utility class for creating random cluster stats.
 */
public final class ClusterStatsUtils {

    private ClusterStatsUtils() {
        // instantiation protection
    }

    public static ClusterStats random() {
        return new ClusterStats(
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger()
        );
    }
}
