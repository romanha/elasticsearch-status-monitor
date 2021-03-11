package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;

import java.util.List;

/**
 * Utility class for creating random analysis reports.
 */
public final class AnalysisReports {

    private AnalysisReports() {
        // instantiation protection
    }

    public static AnalysisReport randomFinished() {
        return randomFinished(List.of(), List.of());
    }

    public static AnalysisReport randomFinished(final List<Problem> problems, final List<Warning> warnings) {
        return AnalysisReport.finished(
                Randoms.generateString("Timestamp "),
                StatusMonitorConfigurations.random(),
                problems,
                warnings,
                ClusterInfos.random(),
                List.of(NodeInfos.random(), NodeInfos.random(), NodeInfos.random())
        );
    }

    public static AnalysisReport randomAborted() {
        return randomAborted(List.of());
    }

    public static AnalysisReport randomAborted(final List<Problem> problems) {
        return AnalysisReport.aborted(
                Randoms.generateString("Timestamp "),
                StatusMonitorConfigurations.random(),
                problems
        );
    }
}
