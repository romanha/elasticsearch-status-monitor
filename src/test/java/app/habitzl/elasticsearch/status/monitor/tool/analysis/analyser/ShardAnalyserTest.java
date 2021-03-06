package app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser;

import app.habitzl.elasticsearch.status.monitor.UnassignedShardInfos;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisResult;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.warnings.UnassignedShardsWarning;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

class ShardAnalyserTest {

    private ShardAnalyser sut;

    @BeforeEach
    void setUp() {
        sut = new ShardAnalyser();
    }

    @Test
    void analyse_noUnassignedShardInfo_returnsEmptyResult() {
        // Given
        // no unassigned shard info

        // When
        AnalysisResult result = sut.analyse(null);

        // Then
        AnalysisResult expected = AnalysisResult.empty();
        assertThat(result, equalTo(expected));
    }

    @Test
    void analyse_unassignedShardInfo_returnsUnassignedShardsWarning() {
        // Given
        UnassignedShardInfo unassignedShardInfo = UnassignedShardInfos.random();

        // When
        AnalysisResult result = sut.analyse(unassignedShardInfo);

        // Then
        UnassignedShardsWarning expectedWarning = UnassignedShardsWarning.create(unassignedShardInfo);
        assertThat(result.getWarnings(), hasItem(expectedWarning));
    }
}