package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;

/**
 * Utility class for creating random cluster settings.
 */
public final class ClusterSettingsUtils {

    private static final int ONE_HUNDRED = 100;

    private ClusterSettingsUtils() {
        // instantiation protection
    }

    public static ClusterSettings random() {
        return random(Randoms.generateInteger(ONE_HUNDRED));
    }

    public static ClusterSettings random(final int minimumMasterNodes) {
        return ClusterSettings.builder()
                              .withMinimumOfRequiredMasterNodesForElection(minimumMasterNodes)
                              .build();
    }
}
