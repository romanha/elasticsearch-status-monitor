package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedReason;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;

/**
 * Utility class for creating random unassigned shard infos.
 */
public final class UnassignedShardInfos {

    private UnassignedShardInfos() {
        // instantiation protection
    }

    public static UnassignedShardInfo random() {
        SecureRandom random = new SecureRandom();

        return new UnassignedShardInfo(
                "test-index-" + random.nextInt(),
                Math.abs(random.nextInt()),
                random.nextBoolean(),
                UnassignedReason.UNKNOWN_REASON,
                Instant.now(),
                "cannot allocated because of reason " + random.nextInt(),
                List.of(NodeAllocationDecisions.random(), NodeAllocationDecisions.random())
        );
    }
}
