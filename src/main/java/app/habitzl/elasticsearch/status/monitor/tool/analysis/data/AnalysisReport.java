package app.habitzl.elasticsearch.status.monitor.tool.analysis.data;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The analysis report model.
 */
@Immutable
public final class AnalysisReport implements Serializable {
    private static final long serialVersionUID = 1L;

    private final StatusMonitorConfiguration configuration;
    private final ReportProgress reportProgress;
    private final List<Problem> problems;
    private final List<Warning> warnings;
    private final ClusterInfo clusterInfo;
    private final List<NodeInfo> nodeInfos;

    public static AnalysisReport aborted(
            final StatusMonitorConfiguration configuration,
            final List<Problem> problems) {
        return new AnalysisReport(configuration, ReportProgress.ABORTED, problems);
    }

    public static AnalysisReport finished(
            final StatusMonitorConfiguration configuration,
            final List<Problem> problems,
            final List<Warning> warnings,
            final ClusterInfo cluster,
            final List<NodeInfo> nodes) {
        List<NodeInfo> sortedNodes = sortNodesByName(nodes);
        return new AnalysisReport(configuration, ReportProgress.FINISHED, problems, warnings, cluster, sortedNodes);
    }

    private static List<NodeInfo> sortNodesByName(final List<NodeInfo> nodes) {
        List<NodeInfo> sortedNodes = new ArrayList<>(nodes);
        sortedNodes.sort(Comparator.comparing(NodeInfo::getNodeName));
        return sortedNodes;
    }

    private AnalysisReport(final StatusMonitorConfiguration configuration, final ReportProgress reportProgress, final List<Problem> problems) {
        this(configuration, reportProgress, problems, List.of(), null, List.of());
    }

    private AnalysisReport(
            final StatusMonitorConfiguration configuration,
            final ReportProgress reportProgress,
            final List<Problem> problems,
            final List<Warning> warnings,
            final ClusterInfo clusterInfo,
            final List<NodeInfo> nodeInfos) {
        this.configuration = configuration;
        this.reportProgress = reportProgress;
        this.problems = List.copyOf(problems);
        this.warnings = List.copyOf(warnings);
        this.clusterInfo = clusterInfo;
        this.nodeInfos = List.copyOf(nodeInfos);
    }

    public StatusMonitorConfiguration getConfiguration() {
        return configuration;
    }

    public ReportProgress getReportProgress() {
        return reportProgress;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public ClusterInfo getClusterInfo() {
        return clusterInfo;
    }

    public List<NodeInfo> getNodeInfos() {
        return nodeInfos;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(final Object o) {
        boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o == null || getClass() != o.getClass()) {
            isEqual = false;
        } else {
            AnalysisReport that = (AnalysisReport) o;
            isEqual = Objects.equals(configuration, that.configuration)
                    && Objects.equals(reportProgress, that.reportProgress)
                    && Objects.equals(problems, that.problems)
                    && Objects.equals(warnings, that.warnings)
                    && Objects.equals(clusterInfo, that.clusterInfo)
                    && Objects.equals(nodeInfos, that.nodeInfos);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(configuration, reportProgress, problems, warnings, clusterInfo, nodeInfos);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AnalysisReport.class.getSimpleName() + "[", "]")
                .add("configuration=" + configuration)
                .add("reportProgress=" + reportProgress)
                .add("problems=" + problems)
                .add("warnings=" + warnings)
                .add("clusterInfo=" + clusterInfo)
                .add("nodeInfos=" + nodeInfos)
                .toString();
    }
}
