package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.cluster;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.ClusterAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.SplitBrainPossibleWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * An Elasticsearch 6 specific strategy of the {@link ClusterAnalyser}.
 */
public class Elasticsearch6ClusterAnalyser extends AbstractClusterAnalyser {

    @Override
    public AnalysisResult analyse(final ClusterSettings settings, final List<NodeInfo> nodes) {
        List<Warning> warnings = new ArrayList<>();

        checkIfClusterSetupIsNotRedundant(settings, nodes).ifPresent(warnings::add);
        checkIfClusterSetupAllowsSplitBrainScenario(settings, nodes).ifPresent(warnings::add);

        return AnalysisResult.create(List.of(), warnings);
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
        return nodes.stream().filter(NodeInfo::isMasterEligibleNode);
    }
}
