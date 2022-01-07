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
                randomClusterName(),
                Randoms.generateEnumValue(ClusterHealthStatus.class),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                randomMasterNodeId(),
                ClusterStatsUtils.random()
        );
    }

    public static ClusterInfo randomHealthy() {
        return new ClusterInfo(
                randomClusterName(),
                ClusterHealthStatus.GREEN,
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                0,
                0,
                randomMasterNodeId(),
                ClusterStatsUtils.random()
        );
    }

    private static String randomClusterName() {
        return Randoms.generateString("test-cluster-");
    }

    private static String randomMasterNodeId() {
        return Randoms.generateString("master-node-id-");
    }
}
