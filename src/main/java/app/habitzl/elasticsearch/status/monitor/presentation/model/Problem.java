package app.habitzl.elasticsearch.status.monitor.presentation.model;

/**
 * Defines all possible problems found by the automatic analysis of this tool.
 */
public enum Problem {

	GENERAL_CONNECTION_FAILURE(
			"General connection failure",
			Constants.GENERAL_CONNECTION_FAILURE_DESCRIPTION,
			Constants.GENERAL_CONNECTION_FAILURE_SOLUTION
	),

	UNAUTHORIZED_CONNECTION_FAILURE(
			"Unauthorized connection",
			Constants.UNAUTHORIZED_CONNECTION_FAILURE_DESCRIPTION,
			Constants.UNAUTHORIZED_CONNECTION_FAILURE_SOLUTION
	);

	private final String title;
	private final String description;
	private final String solution;

	Problem(final String title, final String description, final String solution) {
		this.title = title;
		this.description = description;
		this.solution = solution;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getSolution() {
		return solution;
	}

	private static class Constants {
		private static final String GENERAL_CONNECTION_FAILURE_DESCRIPTION =
				"The tool could not connect to the Elasticsearch cluster.";
		private static final String GENERAL_CONNECTION_FAILURE_SOLUTION =
				"Check the used IP address and port. Verify that the required certificates are available in the keystore of the Java runtime.";

		private static final String UNAUTHORIZED_CONNECTION_FAILURE_DESCRIPTION =
				"The tool is not authorized to connect to the Elasticsearch cluster.";
		private static final String UNAUTHORIZED_CONNECTION_FAILURE_SOLUTION =
				"Check username and password for connecting to the cluster.";
	}
}
