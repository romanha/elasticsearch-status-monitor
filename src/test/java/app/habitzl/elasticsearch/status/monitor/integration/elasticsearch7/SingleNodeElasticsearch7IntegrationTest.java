package app.habitzl.elasticsearch.status.monitor.integration.elasticsearch7;

import app.habitzl.elasticsearch.status.monitor.ExitCode;
import app.habitzl.elasticsearch.status.monitor.integration.AbstractElasticsearchIntegrationTest;
import app.habitzl.elasticsearch.status.monitor.integration.data.ElasticsearchTestVersion;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.ClusterNotRedundantWarning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for a single-node Elasticsearch 7 installation.
 */
@Disabled("Only intended for manual runs against a local Elasticsearch cluster")
public class SingleNodeElasticsearch7IntegrationTest extends AbstractElasticsearchIntegrationTest {

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
        return super.getPort();
    }

    @BeforeEach
    void setUp() {
        useCustomCredentials("elastic", "elastic");
    }

    @Test
    void startAnalysis_singleNode_returnClusterNotRedundantWarning() {
        // Given
        // single node

        // When
        startAnalysis();

        // Then
        assertThatExitCodeEquals(ExitCode.ONLY_WARNINGS_FOUND);
        assertThatClusterIsNotRedundant();
    }

    private void assertThatClusterIsNotRedundant() {
        Warning expectedWarning = ClusterNotRedundantWarning.create(true, true, false);
        getReportAssertions().assertThatWarningExists(expectedWarning);
    }
}
