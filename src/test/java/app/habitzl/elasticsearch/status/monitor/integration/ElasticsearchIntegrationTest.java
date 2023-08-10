package app.habitzl.elasticsearch.status.monitor.integration;

import app.habitzl.elasticsearch.status.monitor.integration.data.ElasticsearchTestVersion;

/**
 * Interface providing required methods for all integration tests.
 */
public interface ElasticsearchIntegrationTest {

    /**
     * Gets the expected Elasticsearch version for this test run.
     */
    default ElasticsearchTestVersion getElasticsearchVersion() {
        throw new IllegalArgumentException("[TEST] The test did not define an expected Elasticsearch version.");
    }

    /**
     * Gets the host to use for this test run.
     */
    String getHost();

    /**
     * Gets the port to use for this test run.
     */
    String getPort();

    /**
     * Calls the Elasticsearch Status Monitor with the configuration options provided for the test run.
     */
    void startAnalysis();
}
