package app.habitzl.elasticsearch.status.monitor.presentation;

import app.habitzl.elasticsearch.status.monitor.presentation.model.StatusReport;

/**
 * Aggregates the status of the Elasticsearch cluster.
 */
public interface StatusAggregator {

	StatusReport createReport();
}
