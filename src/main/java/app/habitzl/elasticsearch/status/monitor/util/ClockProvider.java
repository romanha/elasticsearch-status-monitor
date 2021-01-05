package app.habitzl.elasticsearch.status.monitor.util;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Objects;
import javax.inject.Provider;

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
