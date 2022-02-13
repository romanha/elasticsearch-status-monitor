package app.habitzl.elasticsearch.status.monitor.integration.elasticsearch6;

import app.habitzl.elasticsearch.status.monitor.ExitCode;
import app.habitzl.elasticsearch.status.monitor.integration.AbstractElasticsearchIntegrationTest;
import app.habitzl.elasticsearch.status.monitor.integration.data.ElasticsearchVersion;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.SplitBrainPossibleWarning;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for a 2-node-cluster Elasticsearch 6 installation.
 */
@Disabled("Only intended for manual runs against a local Elasticsearch cluster")
public class TwoNodeClusterElasticsearch6IntegrationTest extends AbstractElasticsearchIntegrationTest {

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

    @Test
    void startAnalysis_twoNodeCluster_returnSplitBrainPossibleWarning() {
        // Given
        String fallbackEndpoints = "localhost:9201";
        addConfigurationOption("--fallbackEndpoints", fallbackEndpoints);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.ONLY_WARNINGS_FOUND);
        assertThatSplitBrainIsPossible();
    }

    private void assertThatSplitBrainIsPossible() {
        Warning expectedWarning = SplitBrainPossibleWarning.create(-1, 2, 2);
        getReportAssertions().assertThatWarningExists(expectedWarning);
    }
}
