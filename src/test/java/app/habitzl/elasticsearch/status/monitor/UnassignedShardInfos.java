package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedReason;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;

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
        return new UnassignedShardInfo(
                "test-index-" + Randoms.generatePositiveInteger(),
                Randoms.generatePositiveInteger(),
                Randoms.generateBoolean(),
                Randoms.generateEnumValue(UnassignedReason.class),
                Instant.now(),
                "cannot allocated because of reason " + Randoms.generatePositiveInteger(),
                List.of(NodeAllocationDecisions.random(), NodeAllocationDecisions.random())
        );
    }
}
