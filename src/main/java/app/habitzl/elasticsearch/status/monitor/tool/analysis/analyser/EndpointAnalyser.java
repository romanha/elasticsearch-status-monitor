package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems.EndpointsNotReachableProblem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.HighRamUsageWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import app.habitzl.elasticsearch.status.monitor.util.HostnameResolver;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Analyses data of the endpoints to find problems.
 */
public class EndpointAnalyser {
    private static final Logger LOG = LogManager.getLogger(EndpointAnalyser.class);

    private static final int RAM_USAGE_PERCENT_THRESHOLD = 80;
    private static final String HOST_PORT_SEPARATOR = ":";
    private static final String HOSTNAME_ENDPOINT_SEPARATOR = "/";

    private final HostnameResolver hostnameResolver;
    private final StatusMonitorConfiguration configuration;

    @Inject
    public EndpointAnalyser(final HostnameResolver hostnameResolver, final StatusMonitorConfiguration configuration) {
        this.hostnameResolver = hostnameResolver;
        this.configuration = configuration;
    }

    public AnalysisResult analyse(final List<EndpointInfo> endpoints) {
        List<Problem> problems = new ArrayList<>();
        List<Warning> warnings = new ArrayList<>();

        findNotReachableEndpoints(endpoints).ifPresent(problems::add);
        findEndpointsWithHighRamUsage(endpoints).ifPresent(warnings::add);

        return AnalysisResult.create(problems, warnings);
    }

    private Optional<EndpointsNotReachableProblem> findNotReachableEndpoints(final List<EndpointInfo> endpointInfos) {
        List<String> configuredEndpoints = configuration.getAllEndpoints()
                .stream()
                .map(this::resolveHostname)
                .collect(Collectors.toList());
        List<String> reachableEndpoints = endpointInfos.stream()
                .map(EndpointInfo::getHttpPublishAddress)
                .flatMap(splitCombinedHostnameEndpointFormat())
                .collect(Collectors.toList());
        List<String> notReachableEndpoints = configuredEndpoints.stream()
                .filter(configuredEndpoint -> !reachableEndpoints.contains(configuredEndpoint))
                .collect(Collectors.toList());
        LOG.debug("Checking if all the configured endpoints {} are part of the reachable endpoints {}.", configuredEndpoints, reachableEndpoints);
        return notReachableEndpoints.isEmpty()
                ? Optional.empty()
                : Optional.of(EndpointsNotReachableProblem.create(notReachableEndpoints));
    }

    private String resolveHostname(final String address) {
        String resolvedAddress = address;
        String[] hostAndPort = address.split(HOST_PORT_SEPARATOR);
        if (hostAndPort.length == 2) {
            String hostname = hostAndPort[0];
            String port = hostAndPort[1];
            Optional<InetAddress> resolvedIpAddress = hostnameResolver.resolve(hostname);
            if (resolvedIpAddress.isPresent()) {
                resolvedAddress = resolvedIpAddress.get().getHostAddress() + HOST_PORT_SEPARATOR + port;
            }
        } else {
            LOG.error("Cannot resolve the invalid endpoint address '{}'.", address);
        }

        return resolvedAddress;
    }

    /**
     * Elasticsearch 7 changed the reported value of the HTTP publish address from [ip:port] to [hostname/ip:port].
     */
    private static Function<String, Stream<? extends String>> splitCombinedHostnameEndpointFormat() {
        return address -> Arrays.stream(address.split(HOSTNAME_ENDPOINT_SEPARATOR));
    }

    private Optional<HighRamUsageWarning> findEndpointsWithHighRamUsage(final List<EndpointInfo> endpoints) {
        Set<String> endpointsWithHighRamUsage =
                endpoints.stream()
                        .filter(endpoint -> endpoint.getRamUsageInPercent() >= RAM_USAGE_PERCENT_THRESHOLD)
                        .map(EndpointInfo::getIpAddress)
                        .collect(Collectors.toSet());
        LOG.debug("Found endpoints with RAM usage over {}%: {}", RAM_USAGE_PERCENT_THRESHOLD, endpointsWithHighRamUsage);
        return endpointsWithHighRamUsage.isEmpty()
                ? Optional.empty()
                : Optional.of(HighRamUsageWarning.create(endpointsWithHighRamUsage));
    }
}
