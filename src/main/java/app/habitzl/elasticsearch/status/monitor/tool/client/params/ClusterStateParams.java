package app.habitzl.elasticsearch.status.monitor.tool.client.params;

/**
 * Parameter list of all values offered by the Elasticsearch {@code /_cluster/state} API.
 */
public final class ClusterStateParams {
    private ClusterStateParams() {
        // instantiation protection
    }

    public static final String API_ENDPOINT = "/_cluster/state";

    public static String onlyRequestMasterNode() {
        return API_ENDPOINT + "/master_node";
    }
}
