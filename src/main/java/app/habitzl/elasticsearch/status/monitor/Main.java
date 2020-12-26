package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.data.ClusterHealth;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public class Main {

	private static final Logger LOG = LogManager.getLogger(Main.class);

	public static void main(final String[] args) {
		LOG.info("Elasticsearch Status Monitor startup.");

		Injector injector = Guice.createInjector(new GuiceModule());
		StatusMonitor statusMonitor = injector.getInstance(StatusMonitor.class);

		LOG.info("Loading cluster information.");

		printClusterHealth(statusMonitor.getClusterHealth());
	}

	private static void printClusterHealth(final ClusterHealth clusterHealth) {
		System.out.println("Cluster: " + clusterHealth.getClusterName());
		System.out.println("  # of nodes: " + clusterHealth.getNumberOfNodes());
		System.out.println("  # of data nodes: " + clusterHealth.getNumberOfDataNodes());
		System.out.println("  Status: " + clusterHealth.getHealthStatus().name());
	}
}
