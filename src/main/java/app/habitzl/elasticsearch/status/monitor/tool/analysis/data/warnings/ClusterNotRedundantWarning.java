/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;

public class ClusterNotRedundantWarning implements Warning {
    private static final long serialVersionUID = 1L;

    public static ClusterNotRedundantWarning create() {
        return new ClusterNotRedundantWarning();
    }

    private ClusterNotRedundantWarning() {
    }

    @Override
    public String getTitle() {
        return "Cluster is not redundant";
    }

    @Override
    public String getDescription() {
        return "The cluster is not setup in a redundant way. An endpoint or node outage can result in data loss.";
    }

    @Override
    public String getSolution() {
        return "Start at least 2 master-eligible nodes on different endpoints.";
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
