package app.habitzl.elasticsearch.status.monitor.integration.data;

/**
 * Defines all supported Elasticsearch versions.
 */
public enum ElasticsearchVersion {
    ELASTICSEARCH_6_8("6.8"),
    ELASTICSEARCH_7_16("7.16");

    private final String version;

    ElasticsearchVersion(final String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
