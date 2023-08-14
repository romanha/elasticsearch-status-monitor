package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.cluster;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.ClusterAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * The default strategy of the {@link ClusterAnalyser}.
 */
public class DefaultClusterAnalyser extends AbstractClusterAnalyser {

    @Override
    public AnalysisResult analyse(final ClusterSettings settings, final List<NodeInfo> nodes) {
        List<Warning> warnings = new ArrayList<>();

        checkIfClusterSetupIsNotRedundant(settings, nodes).ifPresent(warnings::add);

        return AnalysisResult.create(List.of(), warnings);
    }
}
