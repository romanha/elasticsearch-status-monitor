package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.ClusterNotRedundantWarning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.SplitBrainPossibleWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Analyses data of the whole cluster to find problems.
 */
public class ClusterAnalyser {

    public AnalysisResult analyse(final ClusterSettings settings, final List<NodeInfo> nodes) {
        List<Warning> warnings = new ArrayList<>();

        checkIfClusterSetupIsNotRedundant(nodes).ifPresent(warnings::add);
        checkIfClusterSetupAllowsSplitBrainScenario(settings, nodes).ifPresent(warnings::add);

        return AnalysisResult.create(List.of(), warnings);
    }

    /**
     * A cluster is only redundant if there are multiple master-eligible nodes located on different endpoints.
     * If there is only one master-eligible node, the whole cluster fails if this node crashes.
     * If all master-eligible nodes are located on the same endpoint, the whole cluster fails if this endpoint crashes.
     */
    private Optional<ClusterNotRedundantWarning> checkIfClusterSetupIsNotRedundant(final List<NodeInfo> nodes) {
        ClusterNotRedundantWarning warning = null;

        if (!nodes.isEmpty()) {
            if (nodes.size() == 1) {
                warning = ClusterNotRedundantWarning.create();
            } else {
                long numberOfMasterEligibleNodes = getMasterEligibleNodes(nodes).count();
                long numberOfDifferentEndpoints = getMasterEligibleNodes(nodes)
                        .map(NodeInfo::getEndpointInfo)
                        .map(EndpointInfo::getIpAddress)
                        .distinct()
                        .count();
                if (numberOfMasterEligibleNodes < 2 || numberOfDifferentEndpoints < 2) {
                    warning = ClusterNotRedundantWarning.create();
                }
            }
        }

        return Optional.ofNullable(warning);
    }

    /**
     * A split brain scenario is possible when the quorum of master-eligible nodes is lower than {@code (master-eligible nodes / 2) + 1}.
     * This is based on the ES cluster setting "discovery.zen.minimum_master_nodes".
     */
    private Optional<SplitBrainPossibleWarning> checkIfClusterSetupAllowsSplitBrainScenario(final ClusterSettings settings, final List<NodeInfo> nodes) {
        SplitBrainPossibleWarning warning = null;

        int numberOfMasterEligibleNodes = (int) getMasterEligibleNodes(nodes).count();
        if (numberOfMasterEligibleNodes > 1) {
            int requiredQuorum = (int) Math.floor(((float) numberOfMasterEligibleNodes) / 2 + 1);
            int configuredQuorum = settings.getMinimumOfRequiredMasterNodesForElection();
            if (configuredQuorum < requiredQuorum) {
                warning = SplitBrainPossibleWarning.create(configuredQuorum, requiredQuorum, numberOfMasterEligibleNodes);
            }
        }

        return Optional.ofNullable(warning);
    }

    private Stream<NodeInfo> getMasterEligibleNodes(final List<NodeInfo> nodes) {
        return nodes.stream()
                    .filter(NodeInfo::isMasterEligibleNode);
    }
}
