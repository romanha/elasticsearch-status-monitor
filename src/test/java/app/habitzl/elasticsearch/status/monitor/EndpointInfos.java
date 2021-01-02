package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;

import java.security.SecureRandom;

/**
 * Utility class for creating random endpoint infos.
 */
public final class EndpointInfos {

	private EndpointInfos() {
		// instantiation protection
	}

	public static EndpointInfo random() {
		SecureRandom random = new SecureRandom();

		return new EndpointInfo(
				"Address " + random.nextInt(),
				(int) (Math.random() * 100),
				(int) (Math.random() * 100)
		);
	}
}
