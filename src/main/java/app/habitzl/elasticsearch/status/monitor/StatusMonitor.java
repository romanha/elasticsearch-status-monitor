package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.data.ClusterHealth;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public interface StatusMonitor {

	ClusterHealth getClusterHealth();
}
