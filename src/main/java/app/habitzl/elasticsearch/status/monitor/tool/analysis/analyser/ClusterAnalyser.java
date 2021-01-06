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

/**
 * Analyses data of the whole cluster to find problems.
 */
public class ClusterAnalyser {

    public AnalysisResult analyse(final ClusterSettings settings, final List<NodeInfo> nodes) {
        List<Warning> warnings = new ArrayList<>();

        checkIfClusterSetupIsNotRedundant(nodes).ifPresent(warnings::add);
        checkIfClusterSetupAllowsSplitBrainScenario(nodes).ifPresent(warnings::add);

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
                long numberOfMasterEligibleNodes =
                        nodes.stream()
                             .filter(NodeInfo::isMasterEligibleNode)
                             .count();
                long numberOfDifferentEndpoints =
                        nodes.stream()
                             .filter(NodeInfo::isMasterEligibleNode)
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

    // TODO take cluster setting "discovery.zen.minimum_master_nodes" into account
    //      https://localhost:9200/_cluster/settings?include_defaults

    /**
     * A split brain scenario is possible when the quorum of master-eligible nodes is lower than {@code (master-eligible nodes / 2) + 1}.
     * This is based on the ES cluster setting "discovery.zen.minimum_master_nodes".
     */
    private Optional<SplitBrainPossibleWarning> checkIfClusterSetupAllowsSplitBrainScenario(final List<NodeInfo> nodes) {
        SplitBrainPossibleWarning warning = null;
        if (nodes.size() > 1) {

        }

        return Optional.ofNullable(warning);
    }
}
