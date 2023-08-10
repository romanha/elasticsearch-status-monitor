/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.client.data.node;

import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClusterNodeInfoTest {

    @Test
    void createClusterNodeInfo_noMasterNodeInfoAvailable_throwIllegalArgumentException() {
        // Given
        List<NodeInfo> nodeInfosWithoutMasterNode = List.of(NodeInfos.randomNotMasterEligibleDataNode(), NodeInfos.randomNotMasterEligibleDataNode());

        // When
        Executable executable = () -> new ClusterNodeInfo(nodeInfosWithoutMasterNode);

        // Then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void getElasticsearchVersion_masterNodeInfoAvailable_returnElasticsearchVersionOfMasterNode() {
        // Given
        NodeInfo masterNode = NodeInfos.randomMasterNode();
        var sut = new ClusterNodeInfo(List.of(NodeInfos.randomNotMasterEligibleDataNode(), masterNode, NodeInfos.randomNotMasterEligibleDataNode()));

        // When
        String elasticsearchVersion = sut.getElasticsearchVersion();

        // Then
        assertThat(elasticsearchVersion, equalTo(masterNode.getElasticsearchVersion()));
    }
}