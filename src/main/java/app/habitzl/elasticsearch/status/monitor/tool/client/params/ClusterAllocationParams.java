package app.habitzl.elasticsearch.status.monitor.tool.client.params;

/**
 * Parameter list of all values offered by the Elasticsearch {@code /_cluster/allocation/explain} API.
 */
public final class ClusterAllocationParams {
    private ClusterAllocationParams() {
        // instantiation protection
    }

    public static final String API_ENDPOINT = "/_cluster/allocation/explain";
}
