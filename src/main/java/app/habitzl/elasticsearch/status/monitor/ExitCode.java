package app.habitzl.elasticsearch.status.monitor;

/**
 * Defines all possible exit codes of the program.
 */
public enum ExitCode {

    /**
     * No analysis was performed, because the provided configuration parameters do not require an analysis.
     */
    NO_ANALYSIS_REQUIRED(0),

    /**
     * The analysis was completed and did not find and problems or warnings.
     */
    NO_ISSUES_FOUND(0),

    /**
     * The analysis was not completed. (e.g. connection issues)
     */
    ANALYSIS_ABORTED(1),

    /**
     * The analysis found problems.
     */
    PROBLEMS_FOUND(1),

    /**
     * The analysis was completed without finding problems, but some warnings were found.
     */
    ONLY_WARNINGS_FOUND(2),

    /**
     * The analysis was not started due to misconfiguration. (e.g. unknown CLI option)
     */
    MISCONFIGURATION(3);

    private final int exitCode;

    ExitCode(final int exitCode) {
        this.exitCode = exitCode;
    }

    public int value() {
        return exitCode;
    }
}
