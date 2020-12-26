package app.habitzl.elasticsearch.status.monitor.data.node;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public class EndpointInfo implements Serializable {
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
	public String toString() {
		return new StringJoiner(", ", EndpointInfo.class.getSimpleName() + "[", "]")
				.add("ipAddress='" + ipAddress + "'")
				.add("ramUsageInPercent=" + ramUsageInPercent)
				.add("heapUsageInPercent=" + heapUsageInPercent)
				.toString();
	}
}
