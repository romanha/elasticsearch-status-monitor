package app.habitzl.elasticsearch.status.monitor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * Utility class for creating clock instances suitable for tests.
 */
public final class Clocks {

    private Clocks() {
        // instantiation protection
    }

    public static Clock fixedSystemDefault() {
        return Clock.fixed(Instant.now(), ZoneId.systemDefault());
    }

    public static Clock fixedUTC() {
        return Clock.fixed(Instant.now(), ZoneId.of("UTC"));
    }
}
