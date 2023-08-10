package app.habitzl.elasticsearch.status.monitor.integration.data;

/**
 * Defines all supported Elasticsearch versions for integration tests.
 */
public enum ElasticsearchTestVersion {
    ELASTICSEARCH_6_8("6.8"),
    ELASTICSEARCH_7_17("7.17");

    private final String version;

    ElasticsearchTestVersion(final String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
