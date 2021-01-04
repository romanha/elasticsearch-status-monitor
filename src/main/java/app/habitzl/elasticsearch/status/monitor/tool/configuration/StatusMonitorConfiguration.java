package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Data class that holds all configuration of the tool.
 */
public class StatusMonitorConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    static final Boolean DEFAULT_USING_HTTPS = true;
    static final String DEFAULT_HOST = "127.0.0.1";
    static final String DEFAULT_PORT = "9200";

    private Boolean usingHttps;
    private String host;
    private String port;

    public boolean isUsingHttps() {
        return Objects.nonNull(usingHttps) ? usingHttps : DEFAULT_USING_HTTPS;
    }

    public void setUsingHttps(final Boolean usingHttps) {
        this.usingHttps = usingHttps;
    }

    /**
     * Gets the host of the Elasticsearch endpoint.
     */
    public String getHost() {
        return Objects.nonNull(host) ? host : DEFAULT_HOST;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * Gets the HTTP port of the Elasticsearch endpoint.
     */
    public String getPort() {
        return Objects.nonNull(port) ? port : DEFAULT_PORT;
    }

    public void setPort(final String port) {
        this.port = port;
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
            StatusMonitorConfiguration that = (StatusMonitorConfiguration) o;
            isEqual = Objects.equals(usingHttps, that.usingHttps)
                    && Objects.equals(host, that.host)
                    && Objects.equals(port, that.port);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(usingHttps, host, port);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StatusMonitorConfiguration.class.getSimpleName() + "[", "]")
                .add("usingHttps=" + usingHttps)
                .add("host='" + host + "'")
                .add("port=" + port)
                .toString();
    }
}
