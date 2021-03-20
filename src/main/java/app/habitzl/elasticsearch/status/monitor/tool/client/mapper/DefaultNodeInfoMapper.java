package app.habitzl.elasticsearch.status.monitor.tool.client.mapper;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.util.TimeFormatter;

import javax.inject.Inject;
import java.util.List;

public class DefaultNodeInfoMapper implements NodeInfoMapper {

    private final TimeParser timeParser;
    private final TimeFormatter timeFormatter;

    @Inject
    public DefaultNodeInfoMapper(final TimeParser timeParser, final TimeFormatter timeFormatter) {
        this.timeParser = timeParser;
        this.timeFormatter = timeFormatter;
    }

    @Override
    public List<NodeInfo> map(final String nodeInfoJson, final String nodeStatsJson) {
        // TODO implement
        return List.of();
    }
}
