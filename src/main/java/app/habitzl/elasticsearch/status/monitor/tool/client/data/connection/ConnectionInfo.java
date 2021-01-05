package app.habitzl.elasticsearch.status.monitor.tool.client.data.connection;

import org.elasticsearch.rest.RestStatus;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

@Immutable
public final class ConnectionInfo {

    private final ConnectionStatus connectionStatus;
    private final RestStatus restStatus;
    private final String connectionErrorInformation;

    /**
     * Creates a connection info with a successful connection to the Elasticsearch cluster.
     */
    public static ConnectionInfo success(final RestStatus restStatus) {
        return new ConnectionInfo(ConnectionStatus.SUCCESS, restStatus, null);
    }

    /**
     * Creates a connection info without a REST status, because the connection could not be established.
     */
    public static ConnectionInfo error(final ConnectionStatus connectionStatus, final String connectionErrorInformation) {
        return new ConnectionInfo(connectionStatus, null, connectionErrorInformation);
    }

    private ConnectionInfo(
            final ConnectionStatus connectionStatus,
            final @Nullable RestStatus restStatus,
            final @Nullable String connectionErrorInformation) {
        this.connectionStatus = connectionStatus;
        this.restStatus = restStatus;
        this.connectionErrorInformation = connectionErrorInformation;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public Optional<RestStatus> getRestStatus() {
        return Optional.ofNullable(restStatus);
    }

    public Optional<String> getConnectionErrorInformation() {
        return Optional.ofNullable(connectionErrorInformation);
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(final Object o) {
        boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o == null || getClass() != o.getClass()) {
            isEqual = false;
        } else {
            ConnectionInfo that = (ConnectionInfo) o;
            isEqual = Objects.equals(connectionStatus, that.connectionStatus)
                    && Objects.equals(restStatus, that.restStatus);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionStatus, restStatus);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConnectionInfo.class.getSimpleName() + "[", "]")
                .add("connectionStatus=" + connectionStatus)
                .add("restStatus=" + restStatus)
                .toString();
    }
}
