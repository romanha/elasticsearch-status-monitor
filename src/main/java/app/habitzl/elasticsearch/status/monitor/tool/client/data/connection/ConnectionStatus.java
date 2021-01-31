package app.habitzl.elasticsearch.status.monitor.tool.client.data.connection;

/**
 * All possible connection states.
 */
public enum ConnectionStatus {

    /**
     * The tool was able to connect to the Elasticsearch cluster.
     */
    SUCCESS,

    /**
     * The tool could not find the endpoint of the Elasticsearch cluster.
     */
    NOT_FOUND,

    /**
     * The tool and the Elasticsearch cluster could not negotiate the desired level of security.
     */
    SSL_HANDSHAKE_FAILURE,

    /**
     * The tool was not authorized to connect to the Elasticsearch cluster.
     */
    UNAUTHORIZED,

    /**
     * There is no further information about the connection status.
     */
    UNKNOWN;

    /**
     * Determines the connection status from the HTTP status code.
     */
    public static ConnectionStatus fromHttpCode(final int httpStatusCode) {
        ConnectionStatus result;

        switch (httpStatusCode) {
            case 200:
                result = SUCCESS;
                break;
            case 401:
                result = UNAUTHORIZED;
                break;
            case 404:
                result = NOT_FOUND;
                break;
            default:
                result = UNKNOWN;
                break;
        }

        return result;
    }
}
