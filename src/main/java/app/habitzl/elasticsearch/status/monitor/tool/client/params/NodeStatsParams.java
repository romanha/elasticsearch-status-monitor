package app.habitzl.elasticsearch.status.monitor.tool.client.params;

import java.util.StringJoiner;

/**
 * Parameter list of all values offered by the Elasticsearch {@code /_nodes/stats} API.
 */
public final class NodeStatsParams {
    private NodeStatsParams() {
        // instantiation protection
    }

    public static final String API_ENDPOINT = "/_nodes/stats";

    public static final String PARAM_METRIC = "metric";

    public static final String METRIC_FILE_SYSTEM = "fs";
    public static final String METRIC_OPERATING_SYSTEM = "os";
    public static final String METRIC_PROCESS = "process";
    public static final String METRIC_JVM = "jvm";

    public static String allMetrics() {
        return new StringJoiner(",")
                .add(METRIC_FILE_SYSTEM)
                .add(METRIC_OPERATING_SYSTEM)
                .add(METRIC_PROCESS)
                .add(METRIC_JVM)
                .toString();
    }
}
