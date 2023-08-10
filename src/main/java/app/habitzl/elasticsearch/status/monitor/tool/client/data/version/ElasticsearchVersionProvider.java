/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.client.data.version;

import javax.inject.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ElasticsearchVersionProvider implements Provider<ElasticsearchVersion> {
    private static final Logger LOG = LogManager.getLogger(ElasticsearchVersionProvider.class);

    private ElasticsearchVersion version = ElasticsearchVersion.defaultVersion();

    public void updateVersion(final String version) {
        this.version = new ElasticsearchVersion(version);
        LOG.info("Cluster master node running on Elasticsearch version {}.", this.version);
    }

    @Override
    public ElasticsearchVersion get() {
        return version;
    }
}
