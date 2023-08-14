package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.endpoint;

import app.habitzl.elasticsearch.status.monitor.EndpointInfos;
import app.habitzl.elasticsearch.status.monitor.Hosts;
import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.EndpointsNotReachableProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.HighRamUsageWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import app.habitzl.elasticsearch.status.monitor.util.HostnameResolver;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultEndpointAnalyserTest {

    private DefaultEndpointAnalyser sut;
    private HostnameResolver hostnameResolver;
    private StatusMonitorConfiguration configuration;

    @BeforeEach
    void setUp() {
        hostnameResolver = mock(HostnameResolver.class);
        configuration = StatusMonitorConfigurations.random();
        sut = new DefaultEndpointAnalyser(hostnameResolver, configuration);
    }

    @Test
    void analyse_allEndpointsReachable_returnEmpty() {
        // Given
        configuration.setFallbackEndpoints(List.of(Hosts.randomAddress(), Hosts.randomAddress()));
        List<EndpointInfo> endpointInfos = configuration.getAllEndpoints()
                .stream()
                .map(httpPublishAddress -> EndpointInfos.random(httpPublishAddress, 0))
                .collect(Collectors.toList());

        // When
        AnalysisResult result = sut.analyse(endpointInfos);

        // Then
        AnalysisResult expected = AnalysisResult.empty();
        assertThat(result, equalTo(expected));
    }

    @Test
    void analyse_onlyMainEndpointReachable_returnEndpointsNotReachableProblem() {
        // Given
        configuration.setFallbackEndpoints(List.of(Hosts.randomAddress(), Hosts.randomAddress()));
        EndpointInfo endpointInfo = EndpointInfos.random(configuration.getMainEndpoint(), 0);

        // When
        AnalysisResult result = sut.analyse(List.of(endpointInfo));

        // Then
        List<String> fallbackEndpoints = configuration.getFallbackEndpoints();
        var expectedProblem = EndpointsNotReachableProblem.create(fallbackEndpoints);
        assertThat(result.getProblems(), hasItem(expectedProblem));
    }

    @Test
    void analyse_onlyFallbackEndpointsReachable_returnEndpointsNotReachableProblem() {
        // Given
        configuration.setFallbackEndpoints(List.of(Hosts.randomAddress(), Hosts.randomAddress()));
        List<EndpointInfo> endpointInfos = configuration.getFallbackEndpoints()
                .stream()
                .map(httpPublishAddress -> EndpointInfos.random(httpPublishAddress, 0))
                .collect(Collectors.toList());

        // When
        AnalysisResult result = sut.analyse(endpointInfos);

        // Then
        String mainEndpoint = configuration.getMainEndpoint();
        var expectedProblem = EndpointsNotReachableProblem.create(List.of(mainEndpoint));
        assertThat(result.getProblems(), hasItem(expectedProblem));
    }

    @Test
    void analyse_noEndpointsAndConfiguredFallbackHosts_returnEndpointsNotReachableProblem() {
        // Given
        String fallbackEndpoint1 = Hosts.randomAddress();
        String fallbackEndpoint2 = Hosts.randomAddress();
        configuration.setFallbackEndpoints(List.of(fallbackEndpoint1, fallbackEndpoint2));
        List<EndpointInfo> noEndpoints = List.of();

        // When
        AnalysisResult result = sut.analyse(noEndpoints);

        // Then
        List<String> allConfiguredEndpoints = configuration.getAllEndpoints();
        var expectedProblem = EndpointsNotReachableProblem.create(allConfiguredEndpoints);
        assertThat(result.getProblems(), hasItem(expectedProblem));
    }

    @Test
    void analyse_endpointIsReachableViaIpAddressAndConfiguredWithResolvableHostname_returnEmpty() {
        // Given
        configuration.setFallbackEndpoints(List.of());
        String ipAddress = "1.2.3.4";
        String endpointHttpPublishAddress = String.format("%s:%s", ipAddress, configuration.getPort());
        EndpointInfo endpointInfo = EndpointInfos.random(endpointHttpPublishAddress, 0);
        givenHostnameIsResolvedToAddress(configuration.getHost(), ipAddress);

        // When
        AnalysisResult result = sut.analyse(List.of(endpointInfo));

        // Then
        AnalysisResult expected = AnalysisResult.empty();
        assertThat(result, equalTo(expected));
    }

    /**
     * Elasticsearch 7 changed the reported value of the HTTP publish address from [ip:port] to [hostname/ip:port].
     */
    @Test
    void analyse_endpointIsReachableViaHostnamePlusIpAddressAndConfiguredWithResolvableHostname_returnEmpty() {
        // Given
        configuration.setFallbackEndpoints(List.of());
        String hostname = "host.name";
        String ipAddress = "1.2.3.4";
        String endpointHttpPublishAddress = String.format("%s/%s:%s", hostname, ipAddress, configuration.getPort());
        EndpointInfo endpointInfo = EndpointInfos.random(endpointHttpPublishAddress, 0);
        givenHostnameIsResolvedToAddress(configuration.getHost(), ipAddress);

        // When
        AnalysisResult result = sut.analyse(List.of(endpointInfo));

        // Then
        AnalysisResult expected = AnalysisResult.empty();
        assertThat(result, equalTo(expected));
    }

    @Test
    void analyse_endpointIsReachableButConfiguredWithUnresolvableHostname_returnEmpty() {
        // Given
        configuration.setFallbackEndpoints(List.of());
        String ipAddress = "1.2.3.4";
        String endpointHttpPublishAddress = String.format("%s:%s", ipAddress, configuration.getPort());
        EndpointInfo endpointInfo = EndpointInfos.random(endpointHttpPublishAddress, 0);

        // When
        AnalysisResult result = sut.analyse(List.of(endpointInfo));

        // Then
        String mainEndpoint = configuration.getMainEndpoint();
        var expectedProblem = EndpointsNotReachableProblem.create(List.of(mainEndpoint));
        assertThat(result.getProblems(), hasItem(expectedProblem));
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

    private void givenHostnameIsResolvedToAddress(final String hostname, final String ipAddress) {
        InetAddress inetAddress = mock(InetAddress.class);
        when(inetAddress.getHostAddress()).thenReturn(ipAddress);
        when(hostnameResolver.resolve(hostname)).thenReturn(Optional.of(inetAddress));
    }
}