package app.habitzl.elasticsearch.status.monitor.tool.client.params;

/**
 * Parameter list of all values offered by the Elasticsearch {@code /_nodes} API.
 */
public final class NodeInfoParams {
    private NodeInfoParams() {
        // instantiation protection
    }

    public static final String API_ENDPOINT = "/_nodes/os,process,jvm";
}
