package app.habitzl.elasticsearch.status.monitor.tool.client.data.connection;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

@Immutable
public final class ConnectionInfo {

    private final ConnectionStatus connectionStatus;
    private final String connectionErrorInformation;

    /**
     * Creates a connection info with a successful connection to the Elasticsearch cluster.
     */
    public static ConnectionInfo success() {
        return new ConnectionInfo(ConnectionStatus.SUCCESS, null);
    }

    /**
     * Creates an error connection info with details about why the connection could not be established.
     */
    public static ConnectionInfo error(
            final ConnectionStatus connectionStatus,
            final @Nullable String connectionErrorInformation) {
        return new ConnectionInfo(connectionStatus, connectionErrorInformation);
    }

    private ConnectionInfo(
            final ConnectionStatus connectionStatus,
            final @Nullable String connectionErrorInformation) {
        this.connectionStatus = connectionStatus;
        this.connectionErrorInformation = connectionErrorInformation;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
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
                    && Objects.equals(connectionErrorInformation, that.connectionErrorInformation);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionStatus, connectionErrorInformation);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConnectionInfo.class.getSimpleName() + "[", "]")
                .add("connectionStatus=" + connectionStatus)
                .add("connectionErrorInformation='" + connectionErrorInformation + "'")
                .toString();
    }
}
