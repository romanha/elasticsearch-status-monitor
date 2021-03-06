package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.UnassignedShardsWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Analyses data of shards.
 */
public class ShardAnalyser {

    public AnalysisResult analyse(final @Nullable UnassignedShardInfo unassignedShardInfo) {
        List<Warning> warnings = new ArrayList<>();

        checkIfClusterHasUnassignedShards(unassignedShardInfo).ifPresent(warnings::add);

        return AnalysisResult.create(List.of(), warnings);
    }

    private Optional<UnassignedShardsWarning> checkIfClusterHasUnassignedShards(final UnassignedShardInfo unassignedShardInfo) {
        UnassignedShardsWarning warning = null;

        if (Objects.nonNull(unassignedShardInfo)) {
            warning = UnassignedShardsWarning.create(unassignedShardInfo);
        }

        return Optional.ofNullable(warning);
    }
}
