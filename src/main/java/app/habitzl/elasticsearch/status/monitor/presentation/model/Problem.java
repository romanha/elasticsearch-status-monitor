package app.habitzl.elasticsearch.status.monitor.presentation.model;

/**
 * Defines all possible problems found by the automatic analysis of this tool.
 */
public enum Problem {

	CONNECTION_FAILURE(
			"Connection failure",
			Constants.CONNECTION_FAILURE_DESCRIPTION,
			Constants.CONNECTION_FAILURE_SOLUTION
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
		private static final String CONNECTION_FAILURE_DESCRIPTION =
				"The tool could not connect to the Elasticsearch cluster.";
		private static final String CONNECTION_FAILURE_SOLUTION =
				"Check the used IP address and port. Verify that the required certificates are available in the keystore of the Java runtime.";
	}
}
