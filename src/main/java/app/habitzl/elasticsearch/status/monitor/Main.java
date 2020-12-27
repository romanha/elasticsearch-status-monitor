package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.data.node.NodeInfo;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * The tool's main entry point.
 */
public class Main {

	private static final Logger LOG = LogManager.getLogger(Main.class);

	public static void main(final String[] args) {
		LOG.info("Elasticsearch Status Monitor startup.");

		Injector injector = Guice.createInjector(new GuiceModule());
		StatusMonitor statusMonitor = injector.getInstance(StatusMonitor.class);

		LOG.info("Loading cluster information.");

		printClusterHealth(statusMonitor.getClusterInfo());
		printNodeHealth(statusMonitor.getNodeInfo());

		try {
			statusMonitor.closeConnection();
		} catch (IOException e) {
			LOG.warn("Could not safely close the connection to the ES cluster.", e);
		}
	}

	private static void printClusterHealth(final ClusterInfo clusterHealth) {
		System.out.println("Cluster: " + clusterHealth.getClusterName());
		System.out.println("  - # of nodes: " + clusterHealth.getNumberOfNodes());
		System.out.println("  - # of data nodes: " + clusterHealth.getNumberOfDataNodes());
		System.out.println("  - Status: " + clusterHealth.getHealthStatus().name());
		System.out.println("  - active shards: " + clusterHealth.getNumberOfActiveShards());
		System.out.println("  - unassigned shards: " + clusterHealth.getNumberOfUnassignedShards());
	}

	private static void printNodeHealth(final List<NodeInfo> nodeInfos) {
		for (NodeInfo nodeInfo : nodeInfos) {
			System.out.println("Node: " + nodeInfo.getNodeName() + " (" + nodeInfo.getEndpointInfo().getIpAddress() + ")");
			System.out.println("  - process ID: " + nodeInfo.getProcessId());
			System.out.println("  - uptime: " + nodeInfo.getUptime());
			System.out.println("  - is master: " + nodeInfo.isMasterNode());
			System.out.println("  - can become master: " + nodeInfo.isMasterEligibleNode());
			System.out.println("  - has data: " + nodeInfo.isDataNode());
			System.out.println("  - average load: " + nodeInfo.getLoadAverageLast15Minutes());
		}
	}
}
