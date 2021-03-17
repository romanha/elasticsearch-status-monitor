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
                Randoms.generateEnumValue(ClusterHealthStatus.class),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generateString("master-node-id-")
        );
    }

    public static ClusterInfo randomHealthy() {
        return new ClusterInfo(
                Randoms.generateString("test-cluster-"),
                ClusterHealthStatus.GREEN,
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                0,
                0,
                Randoms.generateString("master-node-id-")
        );
    }
}
