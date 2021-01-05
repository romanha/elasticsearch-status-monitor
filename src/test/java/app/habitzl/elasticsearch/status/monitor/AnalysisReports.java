package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;

import java.util.List;

/**
 * Utility class for creating random analysis reports.
 */
public final class AnalysisReports {

    private AnalysisReports() {
        // instantiation protection
    }

    public static AnalysisReport random() {
        return AnalysisReport.create(
                StatusMonitorConfigurations.random(),
                List.of(),
                List.of(),
                ClusterInfos.random(),
                List.of(NodeInfos.random(), NodeInfos.random(), NodeInfos.random())
        );
    }
}
