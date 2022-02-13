package app.habitzl.elasticsearch.status.monitor.integration.elasticsearch6;

import app.habitzl.elasticsearch.status.monitor.ExitCode;
import app.habitzl.elasticsearch.status.monitor.integration.AbstractElasticsearchIntegrationTest;
import app.habitzl.elasticsearch.status.monitor.integration.data.ElasticsearchVersion;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for a 3-node-cluster Elasticsearch 6 installation.
 * <p>
 * Required Elasticsearch configuration:
 * <p>
 * - {@code discovery.zen.minimum_master_nodes: 2}
 */
@Disabled("Only intended for manual runs against a local Elasticsearch cluster")
public class ThreeNodeClusterElasticsearch6IntegrationTest extends AbstractElasticsearchIntegrationTest {

    @Override
    public ElasticsearchVersion getElasticsearchVersion() {
        return ElasticsearchVersion.ELASTICSEARCH_6_8;
    }

    @Override
    public String getHost() {
        return super.getHost();
    }

    @Override
    public String getPort() {
        return super.getPort();
    }

    // TODO This test does not work currently!
    //      The tool needs support for disabling certain warnings.
    //      For this test, "high RAM usage" and "cluster not redundant" (using same host) need to be disabled.
    @Test
    void startAnalysis_threeNodeCluster_returnSplitBrainPossibleWarning() {
        // Given
        String fallbackEndpoints = "localhost:9201,localhost:9202";
        addConfigurationOption("--fallbackEndpoints", fallbackEndpoints);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.NO_ISSUES_FOUND);
        assertThatNoProblemsAndWarningsExist();
    }

    private void assertThatNoProblemsAndWarningsExist() {
        getReportAssertions().assertThatNoProblemsExist();
        getReportAssertions().assertThatNoWarningsExist();
    }
}
