/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import java.util.Objects;
import java.util.Set;

public class HighRamUsageWarning implements Warning {
    private static final long serialVersionUID = 1L;

    private final Set<String> endpointsWithHighRamUsage;

    public static HighRamUsageWarning create(final Set<String> endpointsWithHighRamUsage) {
        return new HighRamUsageWarning(endpointsWithHighRamUsage);
    }

    private HighRamUsageWarning(final Set<String> endpointsWithHighRamUsage) {
        this.endpointsWithHighRamUsage = endpointsWithHighRamUsage;
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
        return "Keep an eye on the endpoints. Check running processes for high memory consumption.";
    }

    @Override
    public String getAdditionalInformation() {
        return "Following endpoints have a high RAM usage: " + String.join(", ", endpointsWithHighRamUsage);
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(final Object o) {
        boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o == null || getClass() != o.getClass()) {
            isEqual = false;
        } else {
            HighRamUsageWarning that = (HighRamUsageWarning) o;
            isEqual = Objects.equals(endpointsWithHighRamUsage, that.endpointsWithHighRamUsage);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpointsWithHighRamUsage);
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
