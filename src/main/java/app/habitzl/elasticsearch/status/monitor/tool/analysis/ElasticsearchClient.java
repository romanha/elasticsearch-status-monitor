package app.habitzl.elasticsearch.status.monitor.tool.analysis;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;

import java.util.List;
import java.util.Optional;

/**
 * This client is capable of querying the Elasticsearch cluster for various metrics.
 */
public interface ElasticsearchClient {

    /**
     * Checks the connection to the ES cluster and returns the status code.
     * Returns empty if there are problems with sending the request or parsing the response.
     */
    ConnectionInfo checkConnection();

    /**
     * Gets the configured cluster settings.
     * Includes both default and actually configured settings.
     */
    Optional<ClusterSettings> getClusterSettings();

    /**
     * Gets information about the whole ES cluster.
     */
    Optional<ClusterInfo> getClusterInfo();

    /**
     * Gets information about each node of the ES cluster.
     */
    List<NodeInfo> getNodeInfo();
}
