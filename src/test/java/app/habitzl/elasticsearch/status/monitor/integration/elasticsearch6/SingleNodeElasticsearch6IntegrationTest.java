package app.habitzl.elasticsearch.status.monitor.integration.elasticsearch6;

import app.habitzl.elasticsearch.status.monitor.ExitCode;
import app.habitzl.elasticsearch.status.monitor.integration.AbstractElasticsearchIntegrationTest;
import app.habitzl.elasticsearch.status.monitor.integration.data.ElasticsearchVersion;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.EndpointsNotReachableProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.ClusterNotRedundantWarning;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Integration tests for a single-node Elasticsearch 6 installation.
 */
@Disabled("Only intended for manual runs against a local Elasticsearch cluster")
public class SingleNodeElasticsearch6IntegrationTest extends AbstractElasticsearchIntegrationTest {

    @Override
    public ElasticsearchVersion getElasticsearchVersion() {
        return ElasticsearchVersion.ELASTICSEARCH_6_8;
    }

    @Test
    void startAnalysisWithUnknownOption_singleNode_returnExitCodeMisconfiguration() {
        // Given
        addConfigurationOption("--unknown", null);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.MISCONFIGURATION);
    }

    @Test
    void startAnalysisWithUnsecureOption_singleNodeWithSecurityEnabled_returnExitCodeAnalysisAborted() {
        // Given
        addConfigurationOption("--unsecure", null);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.ANALYSIS_ABORTED);
    }

    @Test
    void startAnalysisWithUnknownFallbackEndpointOption_singleNode_returnExitCodeProblemsFound() {
        // Given
        String unknownEndpoint = "localhost:9199";
        addConfigurationOption("--fallbackEndpoints", unknownEndpoint);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.PROBLEMS_FOUND);
        assertThatEndpointIsNotReachable(unknownEndpoint);
    }

    @Test
    void startAnalysis_singleNodeWithSecurityEnabled_returnExitCodeOnlyWarningsFound() {
        // Given
        // single node cluster

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.ONLY_WARNINGS_FOUND);
        assertThatClusterIsNotRedundant();
    }

    private void assertThatEndpointIsNotReachable(final String unknownEndpoint) {
        Problem expectedProblem = EndpointsNotReachableProblem.create(List.of(unknownEndpoint));
        getReportAssertions().assertThatProblemExists(expectedProblem);
    }

    private void assertThatClusterIsNotRedundant() {
        Warning expectedWarning = ClusterNotRedundantWarning.create(true, true, false);
        getReportAssertions().assertThatWarningExists(expectedWarning);
    }
}
