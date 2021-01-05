package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.ClusterNotRedundantWarning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.SplitBrainPossibleWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Analyses data of the whole cluster to find problems.
 */
public class ClusterAnalyser {

    public AnalysisResult analyse(final List<NodeInfo> nodes) {
        List<Warning> warnings = new ArrayList<>();

        checkIfClusterSetupIsNotRedundant(nodes).ifPresent(warnings::add);
        checkIfClusterSetupAllowsSplitBrainScenario(nodes).ifPresent(warnings::add);

        return AnalysisResult.create(List.of(), warnings);
    }

    private Optional<ClusterNotRedundantWarning> checkIfClusterSetupIsNotRedundant(final List<NodeInfo> nodes) {
        ClusterNotRedundantWarning warning = null;

        if (!nodes.isEmpty()) {
            if (nodes.size() == 1) {
                warning = ClusterNotRedundantWarning.create();
            } else {
                long numberOfDifferentEndpoints =
                        nodes.stream()
                             .map(NodeInfo::getEndpointInfo)
                             .map(EndpointInfo::getIpAddress)
                             .distinct()
                             .count();
                if (numberOfDifferentEndpoints < 2) {
                    warning = ClusterNotRedundantWarning.create();
                }
            }
        }

        return Optional.ofNullable(warning);
    }

    // TODO take cluster setting "discovery.zen.minimum_master_nodes" into account
    //      https://localhost:9200/_cluster/settings?include_defaults
    private Optional<SplitBrainPossibleWarning> checkIfClusterSetupAllowsSplitBrainScenario(final List<NodeInfo> nodes) {
        return Optional.empty();
    }
}
