package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.cluster;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.ClusterAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.ClusterNotRedundantWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This abstract class provides analysis checks that are relevant for multiple strategies of the {@link ClusterAnalyser}.
 */
public abstract class AbstractClusterAnalyser implements ClusterAnalyser {

    /**
     * A cluster is only redundant if there are multiple master-eligible data nodes located on different endpoints.
     * <ul>
     *   <li>If there is only one master-eligible node, the whole cluster fails if the master-eligible node crashes.</li>
     *   <li>If all master-eligible nodes are located on the same endpoint, the whole cluster fails if this endpoint crashes.</li>
     *   <li>If there is only one data node, the data is lost if the data node crashes.</li>
     *   <li>If all data nodes are located on the same endpoint, the data is lost if this endpoint crashes.</li>
     *   <li>If the quorum of master-eligible nodes is set to the amount of master-eligible nodes, the whole cluster fails
     *   if any single master-eligible node crashes, because it cannot elect a new master.</li>
     * </ul>
     */
    protected Optional<ClusterNotRedundantWarning> checkIfClusterSetupIsNotRedundant(final ClusterSettings settings, final List<NodeInfo> nodes) {
        ClusterNotRedundantWarning warning = null;

        if (!nodes.isEmpty()) {
            long numberOfMasterEligibleNodes = getMasterEligibleNodes(nodes).count();
            long numberOfDifferentMasterEligibleEndpoints = getNumberOfDifferentEndpoints(getMasterEligibleNodes(nodes));
            long numberOfDataNodes = getDataNodes(nodes).count();
            long numberOfDifferentDataEndpoints = getNumberOfDifferentEndpoints(getDataNodes(nodes));
            boolean notEnoughMasterEligibleNodes = notEnoughMasterEligibleNodes(numberOfMasterEligibleNodes, numberOfDifferentMasterEligibleEndpoints);
            boolean notEnoughDataNodes = notEnoughDataNodes(numberOfDataNodes, numberOfDifferentDataEndpoints);
            boolean quorumIsSameAsMasterEligibleNodes = quorumIsSameAsMasterEligibleNodes(settings, numberOfMasterEligibleNodes);
            if (notEnoughMasterEligibleNodes || notEnoughDataNodes || quorumIsSameAsMasterEligibleNodes) {
                warning = ClusterNotRedundantWarning.create(
                        notEnoughMasterEligibleNodes,
                        notEnoughDataNodes,
                        quorumIsSameAsMasterEligibleNodes
                );
            }
        }

        return Optional.ofNullable(warning);
    }

    private Stream<NodeInfo> getMasterEligibleNodes(final List<NodeInfo> nodes) {
        return nodes.stream().filter(NodeInfo::isMasterEligibleNode);
    }

    private Stream<NodeInfo> getDataNodes(final List<NodeInfo> nodes) {
        return nodes.stream().filter(NodeInfo::isDataNode);
    }

    private long getNumberOfDifferentEndpoints(final Stream<NodeInfo> nodes) {
        return nodes.map(NodeInfo::getEndpointInfo)
                .map(EndpointInfo::getIpAddress)
                .distinct()
                .count();
    }

    private boolean notEnoughDataNodes(final long numberOfDataNodes, final long numberOfDifferentDataEndpoints) {
        return numberOfDataNodes < 2 || numberOfDifferentDataEndpoints < 2;
    }

    private boolean notEnoughMasterEligibleNodes(final long numberOfMasterEligibleNodes, final long numberOfDifferentMasterEligibleEndpoints) {
        return numberOfMasterEligibleNodes < 2 || numberOfDifferentMasterEligibleEndpoints < 2;
    }

    private boolean quorumIsSameAsMasterEligibleNodes(final ClusterSettings settings, final long numberOfMasterEligibleNodes) {
        return settings.getMinimumOfRequiredMasterNodesForElection() == numberOfMasterEligibleNodes;
    }
}
