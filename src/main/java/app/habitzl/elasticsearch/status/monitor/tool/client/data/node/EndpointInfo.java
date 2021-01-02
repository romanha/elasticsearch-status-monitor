package app.habitzl.elasticsearch.status.monitor.tool.client.data.node;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Immutable
public final class EndpointInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String ipAddress;
	private final int ramUsageInPercent;
	private final int heapUsageInPercent;

	public EndpointInfo(final String ipAddress, final int ramUsageInPercent, final int heapUsageInPercent) {
		this.ipAddress = ipAddress;
		this.ramUsageInPercent = ramUsageInPercent;
		this.heapUsageInPercent = heapUsageInPercent;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getRamUsageInPercent() {
		return ramUsageInPercent;
	}

	public int getHeapUsageInPercent() {
		return heapUsageInPercent;
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
			isEqual = Objects.equals(ramUsageInPercent, that.ramUsageInPercent)
					&& Objects.equals(heapUsageInPercent, that.heapUsageInPercent)
					&& Objects.equals(ipAddress, that.ipAddress);
		}

		return isEqual;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ipAddress, ramUsageInPercent, heapUsageInPercent);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", EndpointInfo.class.getSimpleName() + "[", "]")
				.add("ipAddress='" + ipAddress + "'")
				.add("ramUsageInPercent=" + ramUsageInPercent)
				.add("heapUsageInPercent=" + heapUsageInPercent)
				.toString();
	}
}
