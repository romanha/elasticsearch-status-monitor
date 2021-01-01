package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterHealthStatus;
import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;

import java.security.SecureRandom;

/**
 * Utility class for creating random cluster infos.
 */
public final class ClusterInfos {

	private ClusterInfos() {
		// instantiation protection
	}

	public static ClusterInfo random() {
		SecureRandom random = new SecureRandom();

		return new ClusterInfo(
				"test-cluster-" + random.nextInt(),
				ClusterHealthStatus.GREEN,
				random.nextInt(),
				random.nextInt(),
				random.nextInt(),
				random.nextInt(),
				random.nextInt()
		);
	}
}
