package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;

import java.security.SecureRandom;

/**
 * Utility class for creating random cluster settings.
 */
public final class ClusterSettingsUtils {

    private static final int ONE_HUNDRED = 100;

    private ClusterSettingsUtils() {
        // instantiation protection
    }

    public static ClusterSettings random() {
        SecureRandom random = new SecureRandom();
        return new ClusterSettings(random.nextInt(ONE_HUNDRED));
    }

    public static ClusterSettings random(final int minimumMasterNodes) {
        return new ClusterSettings(minimumMasterNodes);
    }
}
