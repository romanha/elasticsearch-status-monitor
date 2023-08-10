package app.habitzl.elasticsearch.status.monitor.integration.elasticsearch7;

import app.habitzl.elasticsearch.status.monitor.ExitCode;
import app.habitzl.elasticsearch.status.monitor.integration.AbstractElasticsearchIntegrationTest;
import app.habitzl.elasticsearch.status.monitor.integration.data.ElasticsearchTestVersion;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.GeneralConnectionProblem;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for a not reachable Elasticsearch 7 installation.
 */
@Disabled("Only intended for manual runs against a local Elasticsearch cluster")
public class UnreachableClusterElasticsearch7IntegrationTest extends AbstractElasticsearchIntegrationTest {

    @Override
    public ElasticsearchTestVersion getElasticsearchVersion() {
        return ElasticsearchTestVersion.ELASTICSEARCH_7_17;
    }

    @Override
    public String getHost() {
        return super.getHost();
    }

    @Override
    public String getPort() {
        return "9299";
    }

    @Test
    void startAnalysis_clusterNotReachable_returnAnalysisAbortedMisconfiguration() {
        // Given
        // no Elasticsearch instance running

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.ANALYSIS_ABORTED);
        assertThatClusterIsNotReachable();
    }

    private void assertThatClusterIsNotReachable() {
        Problem expectedProblem = GeneralConnectionProblem.create();
        getReportAssertions().assertThatProblemExists(expectedProblem);
    }
}
