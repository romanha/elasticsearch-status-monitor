package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import javax.inject.Inject;

/**
 * Holds all available analysers.
 */
public class AnalyserProvider {

    private final EndpointAnalyser endpointAnalyser;
    private final ClusterAnalyserProvider clusterAnalyserProvider;
    private final ShardAnalyser shardAnalyser;

    @Inject
    public AnalyserProvider(
            final EndpointAnalyser endpointAnalyser,
            final ClusterAnalyserProvider clusterAnalyserProvider,
            final ShardAnalyser shardAnalyser) {
        this.endpointAnalyser = endpointAnalyser;
        this.clusterAnalyserProvider = clusterAnalyserProvider;
        this.shardAnalyser = shardAnalyser;
    }

    public EndpointAnalyser getEndpointAnalyser() {
        return endpointAnalyser;
    }

    public ClusterAnalyser getClusterAnalyser() {
        return clusterAnalyserProvider.get();
    }

    public ShardAnalyser getShardAnalyser() {
        return shardAnalyser;
    }
}
