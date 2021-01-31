package app.habitzl.elasticsearch.status.monitor.tool.client.params;

/**
 * Parameter list of all values offered by the Elasticsearch {@code /_cluster/settings} API.
 */
public final class ClusterSettingsParams {
    private ClusterSettingsParams() {
        // instantiation protection
    }

    public static final String API_ENDPOINT = "/_cluster/settings";

    public static final String PARAM_INCLUDE_DEFAULTS = "include_defaults";
}
