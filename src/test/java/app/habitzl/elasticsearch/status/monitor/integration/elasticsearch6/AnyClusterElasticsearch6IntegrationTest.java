package app.habitzl.elasticsearch.status.monitor.integration.elasticsearch6;

import app.habitzl.elasticsearch.status.monitor.ExitCode;
import app.habitzl.elasticsearch.status.monitor.Randoms;
import app.habitzl.elasticsearch.status.monitor.integration.AbstractElasticsearchIntegrationTest;
import app.habitzl.elasticsearch.status.monitor.integration.data.ElasticsearchTestVersion;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.EndpointsNotReachableProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.GeneralConnectionProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.UnauthorizedConnectionProblem;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for any reachable Elasticsearch 6 installation.
 */
@Disabled("Only intended for manual runs against a local Elasticsearch cluster")
public class AnyClusterElasticsearch6IntegrationTest extends AbstractElasticsearchIntegrationTest {

    @Override
    public ElasticsearchTestVersion getElasticsearchVersion() {
        return ElasticsearchTestVersion.ELASTICSEARCH_6_8;
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
    void startAnalysisWithHelpOption_always_returnNoAnalysisRequired() {
        // Given
        addConfigurationOption("--help", null);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.NO_ANALYSIS_REQUIRED);
        assertThatReportFileDoesNotExist();
    }

    @Test
    void startAnalysisWithVersionOption_always_returnNoAnalysisRequired() {
        // Given
        addConfigurationOption("--version", null);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.NO_ANALYSIS_REQUIRED);
        assertThatReportFileDoesNotExist();
    }

    @Test
    void startAnalysisWithUnknownOption_reachableCluster_returnExitCodeMisconfiguration() {
        // Given
        addConfigurationOption("--unknown", null);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.MISCONFIGURATION);
        assertThatReportFileDoesNotExist();
    }

    @Test
    void startAnalysisWithUnsecureOption_securedCluster_returnGeneralConnectionProblem() {
        // Given
        addConfigurationOption("--unsecure", null);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.ANALYSIS_ABORTED);
        assertThatClusterIsNotReachable();
    }

    @Test
    void startAnalysisWithInvalidUser_securedCluster_returnUnauthorizedConnectionProblem() {
        // Given
        String invalidUser = Randoms.generateString();
        addConfigurationOption("--username", invalidUser);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.ANALYSIS_ABORTED);
        assertThatAuthorizationFailed();
    }

    @Test
    void startAnalysisWithInvalidPassword_securedCluster_returnUnauthorizedConnectionProblem() {
        // Given
        String invalidPassword = Randoms.generateString();
        addConfigurationOption("--password", invalidPassword);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.ANALYSIS_ABORTED);
        assertThatAuthorizationFailed();
    }

    @Test
    void startAnalysisWithUnknownFallbackEndpointOption_reachableCluster_returnEndpointsNotReachableProblem() {
        // Given
        String unknownEndpoint = "localhost:9199";
        addConfigurationOption("--fallbackEndpoints", unknownEndpoint);

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.PROBLEMS_FOUND);
        assertThatEndpointIsNotReachable(unknownEndpoint);
    }

    private void assertThatClusterIsNotReachable() {
        Problem expectedProblem = GeneralConnectionProblem.create();
        getReportAssertions().assertThatProblemExists(expectedProblem);
    }

    private void assertThatAuthorizationFailed() {
        Problem expectedProblem = UnauthorizedConnectionProblem.create();
        getReportAssertions().assertThatProblemExists(expectedProblem);
    }

    private void assertThatEndpointIsNotReachable(final String unknownEndpoint) {
        Problem expectedProblem = EndpointsNotReachableProblem.create(List.of(unknownEndpoint));
        getReportAssertions().assertThatProblemExists(expectedProblem);
    }
}
