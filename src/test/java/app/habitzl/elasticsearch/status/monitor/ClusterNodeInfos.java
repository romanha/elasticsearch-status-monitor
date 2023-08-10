package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.ClusterNodeInfo;
import java.util.List;

/**
 * Utility class for creating random cluster node infos.
 */
public final class ClusterNodeInfos {

    private ClusterNodeInfos() {
        // instantiation protection
    }

    public static ClusterNodeInfo random() {
        return new ClusterNodeInfo(List.of(NodeInfos.randomMasterNode(), NodeInfos.randomNotMasterEligibleDataNode()));
    }
}
