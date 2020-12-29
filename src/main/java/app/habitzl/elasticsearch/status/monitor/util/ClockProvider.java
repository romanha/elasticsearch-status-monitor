package app.habitzl.elasticsearch.status.monitor.util;

import javax.inject.Provider;
import java.time.Clock;
import java.time.ZoneId;
import java.util.Objects;

/**
 * Provider responsible for returning a clock.
 */
public class ClockProvider implements Provider<Clock> {

	private Clock clock;

	@Override
	public Clock get() {
		return Objects.nonNull(clock) ? clock : createClock();
	}

	private Clock createClock() {
		clock = Clock.system(ZoneId.systemDefault());
		return this.clock;
	}
}
