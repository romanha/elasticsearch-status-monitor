package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;

import javax.annotation.Nullable;

/**
 * Maps the available data to the program's exit code.
 */
public interface ExitCodeMapper {

    /**
     * Determines the exit code based on the configuration and analysis result.
     */
    ExitCode getExitCode(AnalysisStartOption startOption, @Nullable AnalysisReport report);
}
