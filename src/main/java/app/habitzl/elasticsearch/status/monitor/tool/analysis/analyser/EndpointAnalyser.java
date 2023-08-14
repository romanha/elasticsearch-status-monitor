/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;
import java.util.List;

/**
 * Analyses data of the endpoints to find problems.
 */
public interface EndpointAnalyser {

    AnalysisResult analyse(List<EndpointInfo> endpoints);
}
