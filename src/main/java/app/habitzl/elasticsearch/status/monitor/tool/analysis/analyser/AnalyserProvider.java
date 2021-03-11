package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import javax.inject.Inject;

/**
 * Holds all available analysers.
 */
public class AnalyserProvider {

    private final EndpointAnalyser endpointAnalyser;
    private final ClusterAnalyser clusterAnalyser;
    private final ShardAnalyser shardAnalyser;

    @Inject
    public AnalyserProvider(
            final EndpointAnalyser endpointAnalyser,
            final ClusterAnalyser clusterAnalyser,
            final ShardAnalyser shardAnalyser) {
        this.endpointAnalyser = endpointAnalyser;
        this.clusterAnalyser = clusterAnalyser;
        this.shardAnalyser = shardAnalyser;
    }

    public EndpointAnalyser getEndpointAnalyser() {
        return endpointAnalyser;
    }

    public ClusterAnalyser getClusterAnalyser() {
        return clusterAnalyser;
    }

    public ShardAnalyser getShardAnalyser() {
        return shardAnalyser;
    }
}
