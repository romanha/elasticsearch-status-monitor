package app.habitzl.elasticsearch.status.monitor.tool.analysis.data;

/**
 * Defines all possible results of a monitoring attempt.
 * <p>
 * The result also contains the program's exit code.
 */
public enum MonitoringResult {

    /**
     * The analysis was completed and did not find and problems or warnings.
     */
    NO_ISSUES_FOUND(0),

    /**
     * The analysis found problems.
     */
    PROBLEMS_FOUND(1),

    /**
     * The analysis was completed without finding problems, but some warnings were found.
     */
    ONLY_WARNINGS_FOUND(2);

    private final int exitCode;

    MonitoringResult(final int exitCode) {
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }
}
