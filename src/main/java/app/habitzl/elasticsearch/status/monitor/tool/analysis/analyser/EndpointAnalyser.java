package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.HighRamUsageWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Analyses data of the endpoints to find problems.
 */
public class EndpointAnalyser {
    private static final Logger LOG = LogManager.getLogger(EndpointAnalyser.class);

    private static final int RAM_USAGE_PERCENT_THRESHOLD = 80;

    public AnalysisResult analyse(final List<EndpointInfo> endpoints) {
        // todo add check if there are all endpoints available which are configured as main and fallbacks

        List<Warning> warnings = new ArrayList<>();

        findEndpointsWithHighRamUsage(endpoints).ifPresent(warnings::add);

        return AnalysisResult.create(List.of(), warnings);
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
