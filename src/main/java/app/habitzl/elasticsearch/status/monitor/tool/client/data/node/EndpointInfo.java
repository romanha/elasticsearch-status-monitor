package app.habitzl.elasticsearch.status.monitor.tool.client.data.node;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public final class EndpointInfo implements Serializable {
    private static final long serialVersionUID = 2L;

    private final String ipAddress;
    private final String operatingSystemName;
    private final int availableProcessors;
    private final int cpuUsageInPercent;
    @Nullable
    private final Float cpuLoadAverageLast15Minutes;
    private final int ramUsageInPercent;
    private final long ramUsageInBytes;

    public EndpointInfo(
            final String ipAddress,
            final String operatingSystemName,
            final int availableProcessors,
            final int cpuUsageInPercent,
            final @Nullable Float cpuLoadAverageLast15Minutes,
            final int ramUsageInPercent,
            final long ramUsageInBytes) {
        this.ipAddress = ipAddress;
        this.operatingSystemName = operatingSystemName;
        this.availableProcessors = availableProcessors;
        this.cpuUsageInPercent = cpuUsageInPercent;
        this.cpuLoadAverageLast15Minutes = cpuLoadAverageLast15Minutes;
        this.ramUsageInPercent = ramUsageInPercent;
        this.ramUsageInBytes = ramUsageInBytes;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getOperatingSystemName() {
        return operatingSystemName;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public int getCpuUsageInPercent() {
        return cpuUsageInPercent;
    }

    /**
     * The load average indicates the workload of a node.
     * In normal cases, this should be lower than the number of CPU cores on the node.
     * <p>
     * For example, the load value means for a single-core node:
     * <ul>
     * <li>load < 1: No pending processes exist.</li>
     * <li>load = 1: The system does not have idle resources to run more processes.</li>
     * <li>load > 1: Processes are queuing for resources.</li>
     * </ul>
     * If the load exceeds the number of CPU cores:
     * <ul>
     * <li>The CPU utilization or heap memory usage is high or reaches 100%.</li>
     * <li>The query QPS or write QPS spikes or significantly fluctuates.</li>
     * <li>The cluster receives slow queries.</li>
     * </ul>
     */
    @Nullable
    public Float getCpuLoadAverageLast15Minutes() {
        return cpuLoadAverageLast15Minutes;
    }

    public int getRamUsageInPercent() {
        return ramUsageInPercent;
    }

    public long getRamUsageInBytes() {
        return ramUsageInBytes;
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
            EndpointInfo that = (EndpointInfo) o;
            isEqual = Objects.equals(availableProcessors, that.availableProcessors)
                    && Objects.equals(cpuUsageInPercent, that.cpuUsageInPercent)
                    && Objects.equals(cpuLoadAverageLast15Minutes, that.cpuLoadAverageLast15Minutes)
                    && Objects.equals(ramUsageInPercent, that.ramUsageInPercent)
                    && Objects.equals(ramUsageInBytes, that.ramUsageInBytes)
                    && Objects.equals(ipAddress, that.ipAddress)
                    && Objects.equals(operatingSystemName, that.operatingSystemName);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, operatingSystemName, availableProcessors, cpuUsageInPercent, cpuLoadAverageLast15Minutes, ramUsageInPercent, ramUsageInBytes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EndpointInfo.class.getSimpleName() + "[", "]")
                .add("ipAddress='" + ipAddress + "'")
                .add("operatingSystemName='" + operatingSystemName + "'")
                .add("availableProcessors=" + availableProcessors)
                .add("cpuUsageInPercent=" + cpuUsageInPercent)
                .add("cpuLoadAverageLast15Minutes=" + cpuLoadAverageLast15Minutes)
                .add("ramUsageInPercent=" + ramUsageInPercent)
                .add("ramUsageInBytes=" + ramUsageInBytes)
                .toString();
    }
}
