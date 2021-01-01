package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.presentation.StatusAggregator;
import app.habitzl.elasticsearch.status.monitor.presentation.model.StatusReport;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * The tool's main entry point.
 */
public class Main {
	private static final Logger LOG = LogManager.getLogger(Main.class);

	public static void main(final String[] args) {
		LOG.info("Starting up the Elasticsearch Status Monitor.");

		Injector injector = Guice.createInjector(new GuiceModule());
		StatusAggregator statusAggregator = injector.getInstance(StatusAggregator.class);
		ReportGenerator generator = injector.getInstance(ReportGenerator.class);

		StatusReport report = statusAggregator.createReport();
		generator.generate(report);

		LOG.info("Generated report. Closing Elasticsearch Status Monitor.");
		teardown(injector);
	}

	private static void teardown(final Injector injector) {
		closeElasticsearchRestClient(injector);
	}

	private static void closeElasticsearchRestClient(final Injector injector) {
		RestHighLevelClient client = injector.getInstance(RestHighLevelClient.class);
		try {
			client.close();
		} catch (final IOException e) {
			LOG.warn("Could not safely close the connection to the ES cluster.", e);
		}
	}
}
