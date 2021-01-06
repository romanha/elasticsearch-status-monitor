package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;

/**
 * A mapper for data returned via the Elasticsearch {@code /_cluster/settings} API.
 */
public interface ClusterSettingsMapper {
    ClusterSettings map(String jsonData);
}
