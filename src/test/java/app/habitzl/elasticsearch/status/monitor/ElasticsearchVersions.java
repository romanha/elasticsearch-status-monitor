package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.version.ElasticsearchVersion;

/**
 * Utility class for creating Elasticsearch versions.
 */
public final class ElasticsearchVersions {

    private ElasticsearchVersions() {
        // instantiation protection
    }

    public static ElasticsearchVersion unknownVersion() {
        return new ElasticsearchVersion("unknown");
    }

    public static ElasticsearchVersion version6() {
        return new ElasticsearchVersion("6");
    }

    public static ElasticsearchVersion version7() {
        return new ElasticsearchVersion("7");
    }

    public static ElasticsearchVersion version8() {
        return new ElasticsearchVersion("8");
    }
}
