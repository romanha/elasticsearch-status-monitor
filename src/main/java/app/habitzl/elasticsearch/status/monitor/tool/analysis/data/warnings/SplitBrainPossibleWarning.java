/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;

public class SplitBrainPossibleWarning implements Warning {
    private static final long serialVersionUID = 1L;

    public static SplitBrainPossibleWarning create() {
        return new SplitBrainPossibleWarning();
    }

    private SplitBrainPossibleWarning() {
    }

    @Override
    public String getTitle() {
        return "Split brain scenario is possible";
    }

    @Override
    public String getDescription() {
        return "The cluster can run into a split brain scenario with the current setup. This can cause data loss and an inconsistent search behaviour.";
    }

    @Override
    public String getSolution() {
        return "Start enough master-eligible nodes. The setting 'discovery.zen.minimum_master_nodes' should be "
                + "higher than 1 and a quorum of master-eligible nodes: (master-eligible nodes / 2) + 1";
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
