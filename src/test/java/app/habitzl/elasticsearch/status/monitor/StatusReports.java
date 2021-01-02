package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.presentation.model.StatusReport;

import java.util.List;

/**
 * Utility class for creating random status reports.
 */
public final class StatusReports {

	private StatusReports() {
		// instantiation protection
	}

	public static StatusReport random() {
		return StatusReport.create(
				List.of(),
				List.of(),
				ClusterInfos.random(),
				List.of(NodeInfos.random(), NodeInfos.random(), NodeInfos.random())
		);
	}
}
