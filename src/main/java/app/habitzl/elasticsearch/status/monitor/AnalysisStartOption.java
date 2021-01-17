package app.habitzl.elasticsearch.status.monitor;

/**
 * Defines all possible options what to do after analysing the configuration parameters.
 */
public enum AnalysisStartOption {

    /**
     * The provided configuration parameters allow an analysis.
     */
    ANALYSIS_POSSIBLE,

    /**
     * The provided configuration parameters contain errors and do not allow an analysis.
     */
    ANALYSIS_NOT_POSSIBLE,

    /**
     * The provided configuration parameters do not require an analysis.
     */
    ANALYSIS_NOT_REQUESTED
}
