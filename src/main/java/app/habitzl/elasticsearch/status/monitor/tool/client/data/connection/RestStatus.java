package app.habitzl.elasticsearch.status.monitor.tool.client.data.connection;

/**
 * A mapping from HTTP status codes to the REST status.
 */
public enum RestStatus {

    OK,

    UNAUTHORIZED,

    UNKNOWN_STATUS;

    public static RestStatus fromHttpCode(final int httpStatusCode) {
        RestStatus result;

        if (httpStatusCode == 200) {
            result = OK;
        } else if (httpStatusCode == 401) {
            result = UNAUTHORIZED;
        } else {
            result = UNKNOWN_STATUS;
        }

        return result;
    }
}
