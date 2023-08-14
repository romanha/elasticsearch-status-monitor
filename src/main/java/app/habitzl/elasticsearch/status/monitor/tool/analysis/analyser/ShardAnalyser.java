/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;
import javax.annotation.Nullable;

/**
 * Analyses data of shards.
 */
public interface ShardAnalyser {

    AnalysisResult analyse(@Nullable UnassignedShardInfo unassignedShardInfo);
}
