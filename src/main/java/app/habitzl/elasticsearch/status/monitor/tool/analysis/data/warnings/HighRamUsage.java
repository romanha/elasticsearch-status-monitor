/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;

public class HighRamUsage implements Warning {
    private static final long serialVersionUID = 1L;

    public static HighRamUsage create() {
        return new HighRamUsage();
    }

    private HighRamUsage() {
    }

    @Override
    public String getTitle() {
        return "High RAM usage";
    }

    @Override
    public String getDescription() {
        return "At least one endpoint has a high RAM usage.";
    }

    @Override
    public String getSolution() {
        return "Monitor the endpoints. Check running processes for high memory consumption.";
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
