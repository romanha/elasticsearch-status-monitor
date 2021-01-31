package app.habitzl.elasticsearch.status.monitor.tool.client.params;

/**
 * Parameter list of all values offered by the Elasticsearch {@code /_cluster/health} API.
 */
public final class ClusterHealthParams {
    private ClusterHealthParams() {
        // instantiation protection
    }

    public static final String API_ENDPOINT = "/_cluster/health";
}
