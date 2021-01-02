package app.habitzl.elasticsearch.status.monitor.tool.analysis.data;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

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

	private final ReportProgress reportProgress;
	private final List<Problem> problems;
	private final List<Warning> warnings;
	private final ClusterInfo clusterInfo;
	private final List<NodeInfo> nodeInfos;

	public static AnalysisReport aborted(final List<Problem> problems) {
		return new AnalysisReport(ReportProgress.ABORTED, problems);
	}

	public static AnalysisReport create(
			final List<Problem> problems,
			final List<Warning> warnings,
			final ClusterInfo cluster,
			final List<NodeInfo> nodes) {
		List<NodeInfo> sortedNodes = sortNodesByName(nodes);
		return new AnalysisReport(ReportProgress.FINISHED, problems, warnings, cluster, sortedNodes);
	}

	private static List<NodeInfo> sortNodesByName(final List<NodeInfo> nodes) {
		List<NodeInfo> sortedNodes = new ArrayList<>(nodes);
		sortedNodes.sort(Comparator.comparing(NodeInfo::getNodeName));
		return sortedNodes;
	}

	private AnalysisReport(final ReportProgress reportProgress, final List<Problem> problems) {
		this(reportProgress, problems, List.of(), null, List.of());
	}

	private AnalysisReport(
			final ReportProgress reportProgress,
			final List<Problem> problems,
			final List<Warning> warnings,
			final ClusterInfo clusterInfo,
			final List<NodeInfo> nodeInfos) {
		this.reportProgress = reportProgress;
		this.problems = List.copyOf(problems);
		this.warnings = List.copyOf(warnings);
		this.clusterInfo = clusterInfo;
		this.nodeInfos = List.copyOf(nodeInfos);
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
			AnalysisReport report = (AnalysisReport) o;
			isEqual = Objects.equals(reportProgress, report.reportProgress)
					&& Objects.equals(problems, report.problems)
					&& Objects.equals(warnings, report.warnings)
					&& Objects.equals(clusterInfo, report.clusterInfo)
					&& Objects.equals(nodeInfos, report.nodeInfos);
		}

		return isEqual;
	}

	@Override
	public int hashCode() {
		return Objects.hash(reportProgress, problems, warnings, clusterInfo, nodeInfos);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", AnalysisReport.class.getSimpleName() + "[", "]")
				.add("reportProgress=" + reportProgress)
				.add("problems=" + problems)
				.add("warnings=" + warnings)
				.add("clusterInfo=" + clusterInfo)
				.add("nodeInfos=" + nodeInfos)
				.toString();
	}
}
