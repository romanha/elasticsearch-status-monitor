package app.habitzl.elasticsearch.status.monitor.presentation.model;

import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The status report model.
 */
@Immutable
public final class StatusReport implements Serializable {
	private static final long serialVersionUID = 1L;

	private final ReportProgress reportProgress;
	private final List<Problem> problems;
	private final List<Warning> warnings;
	private final ClusterInfo clusterInfo;
	private final List<NodeInfo> nodeInfos;

	public static StatusReport aborted(final List<Problem> problems) {
		return new StatusReport(ReportProgress.ABORTED, problems);
	}

	public static StatusReport create(
			final List<Problem> problems,
			final List<Warning> warnings,
			final ClusterInfo cluster,
			final List<NodeInfo> nodes) {
		List<NodeInfo> sortedNodes = sortNodesByName(nodes);
		return new StatusReport(ReportProgress.FINISHED, problems, warnings, cluster, sortedNodes);
	}

	private static List<NodeInfo> sortNodesByName(final List<NodeInfo> nodes) {
		List<NodeInfo> sortedNodes = new ArrayList<>(nodes);
		sortedNodes.sort(Comparator.comparing(NodeInfo::getNodeName));
		return sortedNodes;
	}

	private StatusReport(final ReportProgress reportProgress, final List<Problem> problems) {
		this(reportProgress, problems, List.of(), null, List.of());
	}

	private StatusReport(
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
			StatusReport report = (StatusReport) o;
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
		return new StringJoiner(", ", StatusReport.class.getSimpleName() + "[", "]")
				.add("reportProgress=" + reportProgress)
				.add("problems=" + problems)
				.add("warnings=" + warnings)
				.add("clusterInfo=" + clusterInfo)
				.add("nodeInfos=" + nodeInfos)
				.toString();
	}
}
