package app.habitzl.elasticsearch.status.monitor.mapper;

import app.habitzl.elasticsearch.status.monitor.data.node.NodeInfo;

import java.util.Map;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public interface NodeInfoParser {
	NodeInfo parse(Map<String, Object> data);
}
