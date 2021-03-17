package app.habitzl.elasticsearch.status.monitor.tool.client.data.node;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public final class NodeStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int availableBytesOnFileSystem;
    private final int cpuUsageInPercent;
    private final Duration uptime;
    private final String uptimeFormatted;
    private final int heapUsageInPercent;
    private final int heapUsageInBytes;
    private final int maximumHeapInBytes;

    public NodeStats(
            final int availableBytesOnFileSystem,
            final int cpuUsageInPercent,
            final Duration uptime,
            final String uptimeFormatted,
            final int heapUsageInPercent,
            final int heapUsageInBytes,
            final int maximumHeapInBytes) {
        this.availableBytesOnFileSystem = availableBytesOnFileSystem;
        this.cpuUsageInPercent = cpuUsageInPercent;
        this.uptime = uptime;
        this.uptimeFormatted = uptimeFormatted;
        this.heapUsageInPercent = heapUsageInPercent;
        this.heapUsageInBytes = heapUsageInBytes;
        this.maximumHeapInBytes = maximumHeapInBytes;
    }

    public int getAvailableBytesOnFileSystem() {
        return availableBytesOnFileSystem;
    }

    public int getCpuUsageInPercent() {
        return cpuUsageInPercent;
    }

    public Duration getUptime() {
        return uptime;
    }

    public String getUptimeFormatted() {
        return uptimeFormatted;
    }

    public int getHeapUsageInPercent() {
        return heapUsageInPercent;
    }

    public int getHeapUsageInBytes() {
        return heapUsageInBytes;
    }

    public int getMaximumHeapInBytes() {
        return maximumHeapInBytes;
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
            NodeStats nodeStats = (NodeStats) o;
            isEqual = Objects.equals(availableBytesOnFileSystem, nodeStats.availableBytesOnFileSystem)
                    && Objects.equals(cpuUsageInPercent, nodeStats.cpuUsageInPercent)
                    && Objects.equals(heapUsageInPercent, nodeStats.heapUsageInPercent)
                    && Objects.equals(heapUsageInBytes, nodeStats.heapUsageInBytes)
                    && Objects.equals(maximumHeapInBytes, nodeStats.maximumHeapInBytes)
                    && Objects.equals(uptime, nodeStats.uptime);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableBytesOnFileSystem, cpuUsageInPercent, uptime, heapUsageInPercent, heapUsageInBytes, maximumHeapInBytes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NodeStats.class.getSimpleName() + "[", "]")
                .add("availableBytesOnFileSystem=" + availableBytesOnFileSystem)
                .add("cpuUsageInPercent=" + cpuUsageInPercent)
                .add("uptime=" + uptime)
                .add("heapUsageInPercent=" + heapUsageInPercent)
                .add("heapUsageInBytes=" + heapUsageInBytes)
                .add("maximumHeapInBytes=" + maximumHeapInBytes)
                .toString();
    }
}
