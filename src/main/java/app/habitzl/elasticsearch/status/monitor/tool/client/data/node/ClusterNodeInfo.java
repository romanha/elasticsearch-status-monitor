/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.client.data.node;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import javax.annotation.concurrent.Immutable;

@Immutable
public class ClusterNodeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String elasticsearchVersion;
    private final List<NodeInfo> nodeInfos;

    public ClusterNodeInfo(final List<NodeInfo> nodeInfos) {
        this.elasticsearchVersion = getElasticsearchVersionOfMasterNode(nodeInfos);
        this.nodeInfos = List.copyOf(nodeInfos);
    }

    public String getElasticsearchVersion() {
        return elasticsearchVersion;
    }

    public List<NodeInfo> getNodeInfos() {
        return nodeInfos;
    }

    private static String getElasticsearchVersionOfMasterNode(final List<NodeInfo> nodeInfos) {
        return nodeInfos.stream()
                .filter(NodeInfo::isMasterNode)
                .findFirst()
                .map(NodeInfo::getElasticsearchVersion)
                .orElseThrow(() -> new IllegalArgumentException("Retrieved node infos did not contain a master node!"));
    }

    @Override
    public boolean equals(final Object o) {
        boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o == null || getClass() != o.getClass()) {
            isEqual = false;
        } else {
            ClusterNodeInfo that = (ClusterNodeInfo) o;
            isEqual = Objects.equals(elasticsearchVersion, that.elasticsearchVersion)
                    && Objects.equals(nodeInfos, that.nodeInfos);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(elasticsearchVersion, nodeInfos);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ClusterNodeInfo.class.getSimpleName() + "[", "]")
                .add("elasticsearchVersion='" + elasticsearchVersion + "'")
                .add("nodeInfos=" + nodeInfos)
                .toString();
    }
}
