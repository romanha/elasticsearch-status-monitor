package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
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
    static final String DEFAULT_USERNAME = "admin";
    static final String DEFAULT_PASSWORD = "admin";
    static final String DEFAULT_REPORT_FILES_PATH = "reports";

    static final String HOST_PORT_SEPARATOR = ":";

    public static StatusMonitorConfiguration defaultConfig() {
        return new StatusMonitorConfiguration();
    }

    private Boolean usingHttps;
    private String host;
    private String port;
    private List<String> fallbackEndpoints;
    private String username;
    private String password;
    private String reportFilesPath;

    /**
     * Gets the information whether the tool should use HTTPS to connect to the Elasticsearch endpoint.
     */
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

    /**
     * Gets the main endpoint defined by the {@link #host} and {@link #port} parameters.
     * The endpoint is returned in the format {@code host:port}.
     */
    public String getMainEndpoint() {
        return getHost() + HOST_PORT_SEPARATOR + getPort();
    }

    /**
     * Gets the list of fallback endpoints of the Elasticsearch cluster in case the main endpoint is not reachable.
     * The endpoints are returned in the format {@code host:port}.
     */
    public List<String> getFallbackEndpoints() {
        return Objects.isNull(fallbackEndpoints) ? List.of() : List.copyOf(fallbackEndpoints);
    }

    public void setFallbackEndpoints(final List<String> fallbackEndpoints) {
        this.fallbackEndpoints = fallbackEndpoints;
    }

    /**
     * Gets the list of all endpoints, which includes the main endpoint and all fallback endpoints.
     * The endpoints are returned in the format {@code host:port}.
     */
    public List<String> getAllEndpoints() {
        List<String> allEndpoints = Lists.asList(getMainEndpoint(), getFallbackEndpoints().toArray(String[]::new));
        return List.copyOf(allEndpoints);
    }

    /**
     * Gets the username of the Elasticsearch user.
     */
    public String getUsername() {
        return Objects.nonNull(username) ? username : DEFAULT_USERNAME;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Gets the password of the Elasticsearch user.
     */
    public String getPassword() {
        return Objects.nonNull(password) ? password : DEFAULT_PASSWORD;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Gets the path to the location of the generated report files.
     */
    public String getReportFilesPath() {
        return Objects.nonNull(reportFilesPath) ? reportFilesPath : DEFAULT_REPORT_FILES_PATH;
    }

    public void setReportFilesPath(final String reportFilesPath) {
        this.reportFilesPath = reportFilesPath;
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
                    && Objects.equals(port, that.port)
                    && Objects.equals(fallbackEndpoints, that.fallbackEndpoints)
                    && Objects.equals(username, that.username)
                    && Objects.equals(password, that.password)
                    && Objects.equals(reportFilesPath, that.reportFilesPath);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(usingHttps, host, port, fallbackEndpoints, username, password, reportFilesPath);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StatusMonitorConfiguration.class.getSimpleName() + "[", "]")
                .add("usingHttps=" + usingHttps)
                .add("host='" + host + "'")
                .add("port='" + port + "'")
                .add("fallbackEndpoints=" + fallbackEndpoints)
                .add("username='" + username + "'")
                .add("password='" + toStringPassword() + "'")
                .add("reportFilesPath='" + reportFilesPath + "'")
                .toString();
    }

    private String toStringPassword() {
        return Objects.isNull(password) ? null : Integer.toString(Objects.hash(password));
    }
}
