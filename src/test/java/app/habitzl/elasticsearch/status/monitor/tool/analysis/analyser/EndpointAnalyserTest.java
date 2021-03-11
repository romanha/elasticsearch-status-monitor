package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.EndpointInfos;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.HighRamUsageWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

class EndpointAnalyserTest {

    private EndpointAnalyser sut;

    @BeforeEach
    void setUp() {
        sut = new EndpointAnalyser();
    }

    @Test
    void analyse_noEndpoints_returnsEmptyResult() {
        // Given
        List<EndpointInfo> noEndpoints = List.of();

        // When
        AnalysisResult result = sut.analyse(noEndpoints);

        // Then
        AnalysisResult expected = AnalysisResult.empty();
        assertThat(result, equalTo(expected));
    }

    @Test
    void analyse_endpointsWithHighRamUsage_returnsHighRamUsageWarningWithAffectedEndpoints() {
        // Given
        EndpointInfo badEndpoint1 = EndpointInfos.random(80);
        EndpointInfo badEndpoint2 = EndpointInfos.random(100);
        EndpointInfo goodEndpoint1 = EndpointInfos.random(0);
        EndpointInfo goodEndpoint2 = EndpointInfos.random(79);
        List<EndpointInfo> allEndpoints = List.of(badEndpoint1, badEndpoint2, goodEndpoint1, goodEndpoint2);

        // When
        AnalysisResult result = sut.analyse(allEndpoints);

        // Then
        Set<String> badEndpointAddresses = Set.of(badEndpoint1.getIpAddress(), badEndpoint2.getIpAddress());
        HighRamUsageWarning expectedWarning = HighRamUsageWarning.create(badEndpointAddresses);
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }
}