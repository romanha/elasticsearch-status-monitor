/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;

public class ClusterNotFullyOperationalProblem implements Problem {
    private static final long serialVersionUID = 1L;

    public static ClusterNotFullyOperationalProblem create() {
        return new ClusterNotFullyOperationalProblem();
    }

    private ClusterNotFullyOperationalProblem() {
    }

    @Override
    public String getTitle() {
        return "Cluster not fully operational";
    }

    @Override
    public String getDescription() {
        return "The Elasticsearch cluster reports that its service is unavailable.";
    }

    @Override
    public String getSolution() {
        return "The cluster must have an active master and the number of running master-eligible nodes "
                + "must satisfy the 'discovery.zen.minimum_master_nodes' setting.";
    }

    @Override
    public String getAdditionalInformation() {
        return "The cluster behaviour depends on the setting 'discovery.zen.no_master_block'."
                + System.lineSeparator()
                + "By default a not fully operational cluster only rejects write operations. "
                + "Read operations succeed, but may return outdated or incomplete results."
                + System.lineSeparator()
                + "If the setting is configured to 'all', the cluster rejects both read and write operations.";
    }

    @Override
    public boolean equals(final Object o) {
        boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else {
            isEqual = o != null && getClass() == o.getClass();
        }

        return isEqual;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
