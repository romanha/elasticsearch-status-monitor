package app.habitzl.elasticsearch.status.monitor.tool.analysis;

import app.habitzl.elasticsearch.status.monitor.AnalysisStartOption;
import app.habitzl.elasticsearch.status.monitor.ExitCode;
import app.habitzl.elasticsearch.status.monitor.ExitCodeMapper;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.ReportProgress;

import javax.annotation.Nullable;
import java.util.Objects;

public class DefaultExitCodeMapper implements ExitCodeMapper {

    @Override
    public ExitCode getExitCode(final AnalysisStartOption startOption, final @Nullable AnalysisReport report) {
        ExitCode result;
        switch (startOption) {
            case ANALYSIS_POSSIBLE:
                result = determineExitCode(report);
                break;
            case ANALYSIS_NOT_REQUESTED:
                result = ExitCode.NO_ANALYSIS_REQUIRED;
                break;
            case ANALYSIS_NOT_POSSIBLE:
            default:
                result = ExitCode.MISCONFIGURATION;
                break;
        }

        return result;
    }

    private ExitCode determineExitCode(final @Nullable AnalysisReport report) {
        ExitCode result = ExitCode.NO_ISSUES_FOUND;
        if (Objects.isNull(report)) {
            result = ExitCode.MISCONFIGURATION;
        } else if (wasAborted(report)) {
            result = ExitCode.ANALYSIS_ABORTED;
        } else if (hasFoundProblems(report)) {
            result = ExitCode.PROBLEMS_FOUND;
        } else if (hasFoundWarnings(report)) {
            result = ExitCode.ONLY_WARNINGS_FOUND;
        }

        return result;
    }

    private boolean wasAborted(final AnalysisReport report) {
        return report.getReportProgress() == ReportProgress.ABORTED;
    }

    private boolean hasFoundProblems(final AnalysisReport report) {
        return !report.getProblems().isEmpty();
    }

    private boolean hasFoundWarnings(final AnalysisReport report) {
        return !report.getWarnings().isEmpty();
    }
}
