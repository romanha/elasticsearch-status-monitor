package app.habitzl.elasticsearch.status.monitor.presentation.model;

/**
 * Defines all possible warnings found by the automatic analysis of this tool.
 */
public enum Warning {

	HIGH_RAM_USAGE(
			"High RAM usage",
			Constants.HIGH_RAM_USAGE_DESCRIPTION,
			Constants.HIGH_RAM_USAGE_SOLUTION
	);

	private final String title;
	private final String description;
	private final String solution;

	Warning(final String title, final String description, final String solution) {
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
		private static final String HIGH_RAM_USAGE_DESCRIPTION =
				"At least one endpoint has a high RAM usage.";
		private static final String HIGH_RAM_USAGE_SOLUTION =
				"Monitor the endpoint. Check the running processes for high memory consumption.";
	}
}
