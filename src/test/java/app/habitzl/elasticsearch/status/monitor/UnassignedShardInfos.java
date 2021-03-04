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
                Randoms.generateString("test-index-"),
                Randoms.generatePositiveInteger(),
                Randoms.generateBoolean(),
                Randoms.generateEnumValue(UnassignedReason.class),
                Instant.now(),
                Randoms.generateString("cannot allocated because of reason "),
                List.of(NodeAllocationDecisions.random(), NodeAllocationDecisions.random())
        );
    }
}
