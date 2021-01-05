package app.habitzl.elasticsearch.status.monitor.tool;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;

/**
 * Aggregates and analyses the status of the Elasticsearch cluster.
 */
public interface StatusAnalyser {

    AnalysisReport createReport();
}
