package app.habitzl.elasticsearch.status.monitor.tool.client.params;

/**
 * Parameter list of all values offered by the Elasticsearch {@code /_cluster/stats} API.
 */
public final class ClusterStatsParams {
    private ClusterStatsParams() {
        // instantiation protection
    }

    public static final String API_ENDPOINT = "/_cluster/stats";
}
