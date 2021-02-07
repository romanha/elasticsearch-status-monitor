package app.habitzl.elasticsearch.status.monitor.tool.client.data.shard;

/**
 * All possible reasons for why a shard can be unassigned.
 */
public enum UnassignedReason {

    /**
     * The tool could not identify the reason why the shard is unassigned.
     */
    UNKNOWN_REASON,

    /**
     * Unassigned as a result of a failed allocation of the shard.
     */
    ALLOCATION_FAILED,

    /**
     * Unassigned as a result of a full cluster recovery.
     */
    CLUSTER_RECOVERED,

    /**
     * Unassigned as a result of importing a dangling index.
     */
    DANGLING_INDEX_IMPORTED,

    /**
     * Unassigned as a result of restoring into a closed index.
     */
    EXISTING_INDEX_RESTORED,

    /**
     * Unassigned as a result of an API creation of an index.
     */
    INDEX_CREATED,

    /**
     * Unassigned as a result of opening a closed index.
     */
    INDEX_REOPENED,

    /**
     * Unassigned as a result of restoring into a new index.
     */
    NEW_INDEX_RESTORED,

    /**
     * Unassigned as a result of the node hosting it leaving the cluster.
     */
    NODE_LEFT,

    /**
     * A better replica location is identified and causes the existing replica allocation to be cancelled.
     */
    REALLOCATED_REPLICA,

    /**
     * When a shard moves from started back to initializing.
     */
    REINITIALIZED,

    /**
     * Unassigned as a result of explicit addition of a replica.
     */
    REPLICA_ADDED,

    /**
     * Unassigned as a result of explicit cancel reroute command.
     */
    REROUTE_CANCELLED
}
