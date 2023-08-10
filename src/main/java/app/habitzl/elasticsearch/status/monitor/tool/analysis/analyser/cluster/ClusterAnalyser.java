/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.cluster;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import java.util.List;

/**
 * Analyses data of the whole cluster to find problems.
 */
public interface ClusterAnalyser {

    AnalysisResult analyse(ClusterSettings settings, List<NodeInfo> nodes);
}
