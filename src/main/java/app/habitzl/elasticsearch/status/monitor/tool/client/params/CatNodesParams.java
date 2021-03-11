package app.habitzl.elasticsearch.status.monitor.tool.client.params;

import java.util.StringJoiner;

/**
 * Parameter list of all values offered by the Elasticsearch {@code /_cat/nodes} API.
 */
// TODO CAT APIs are not intended for machine usage, as they return human readable data.
//      Instead, switch to the /_cluster/state and /_nodes and /_nodes/stats APIs.
public final class CatNodesParams {
    private CatNodesParams() {
        // instantiation protection
    }

    public static final String API_ENDPOINT = "/_cat/nodes";

    public static final String PARAM_COLUMNS = "h";
    public static final String PARAM_FULL_ID = "full_id";

    public static final String IP_COLUMN = "ip";
    public static final String RAM_PERCENT_COLUMN = "ram.percent";
    public static final String HEAP_PERCENT_COLUMN = "heap.percent";
    public static final String NODE_PROCESS_ID_COLUMN = "pid";
    public static final String NODE_ID_COLUMN = "nodeId";
    public static final String NODE_NAME_COLUMN = "name";
    public static final String NODE_MASTER_COLUMN = "master";
    public static final String NODE_ROLE_COLUMN = "node.role";
    public static final String NODE_UPTIME_COLUMN = "uptime";
    public static final String AVERAGE_LOAD_COLUMN = "load_15m";

    public static String allColumns() {
        return new StringJoiner(",")
                .add(IP_COLUMN)
                .add(RAM_PERCENT_COLUMN)
                .add(HEAP_PERCENT_COLUMN)
                .add(NODE_PROCESS_ID_COLUMN)
                .add(NODE_ID_COLUMN)
                .add(NODE_NAME_COLUMN)
                .add(NODE_MASTER_COLUMN)
                .add(NODE_ROLE_COLUMN)
                .add(NODE_UPTIME_COLUMN)
                .add(AVERAGE_LOAD_COLUMN)
                .toString();
    }
}
