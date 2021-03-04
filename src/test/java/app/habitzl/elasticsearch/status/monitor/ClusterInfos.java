package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterHealthStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;

/**
 * Utility class for creating random cluster infos.
 */
public final class ClusterInfos {

    private ClusterInfos() {
        // instantiation protection
    }

    public static ClusterInfo random() {
        return new ClusterInfo(
                Randoms.generateString("test-cluster-"),
                ClusterHealthStatus.GREEN,
                Randoms.generateInteger(),
                Randoms.generateInteger(),
                Randoms.generateInteger(),
                Randoms.generateInteger(),
                Randoms.generateInteger(),
                Randoms.generateInteger()
        );
    }
}
