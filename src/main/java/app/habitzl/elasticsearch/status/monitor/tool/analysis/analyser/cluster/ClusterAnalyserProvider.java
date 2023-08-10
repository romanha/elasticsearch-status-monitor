/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.cluster;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.version.ElasticsearchMajorVersion;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.version.ElasticsearchVersionProvider;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Provides a cluster provider based on the Elasticsearch version.
 */
public class ClusterAnalyserProvider implements Provider<ClusterAnalyser> {

    private final ElasticsearchVersionProvider elasticsearchVersionProvider;
    private final ClusterAnalyser defaultClusterAnalyser;
    private final Map<ElasticsearchMajorVersion, ClusterAnalyser> clusterAnalysers;

    @Inject
    public ClusterAnalyserProvider(
            final ElasticsearchVersionProvider elasticsearchVersionProvider,
            final DefaultClusterAnalyser defaultClusterAnalyser,
            final Elasticsearch6ClusterAnalyser elasticsearch6ClusterAnalyser) {
        this.elasticsearchVersionProvider = elasticsearchVersionProvider;
        this.defaultClusterAnalyser = defaultClusterAnalyser;
        this.clusterAnalysers = Map.ofEntries(Map.entry(ElasticsearchMajorVersion.SIX, elasticsearch6ClusterAnalyser));
    }

    @Override
    public ClusterAnalyser get() {
        return clusterAnalysers.getOrDefault(elasticsearchVersionProvider.get().getMajorVersion(), defaultClusterAnalyser);
    }
}
