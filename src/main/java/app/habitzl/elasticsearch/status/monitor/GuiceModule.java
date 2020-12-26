package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.ElasticsearchStatusMonitor;
import app.habitzl.elasticsearch.status.monitor.tool.connection.ElasticsearchRestClientFactory;
import app.habitzl.elasticsearch.status.monitor.tool.connection.RestClientFactory;
import app.habitzl.elasticsearch.status.monitor.tool.connection.RestClientProvider;
import app.habitzl.elasticsearch.status.monitor.tool.mapper.JsonContentResponseMapper;
import app.habitzl.elasticsearch.status.monitor.tool.mapper.DefaultNodeInfoParser;
import app.habitzl.elasticsearch.status.monitor.tool.NodeInfoParser;
import app.habitzl.elasticsearch.status.monitor.tool.ResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * A Google Guice module for defining bindings for the projects dependency injection.
 */
class GuiceModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(StatusMonitor.class).to(ElasticsearchStatusMonitor.class).in(Singleton.class);

		// REST client
		bind(RestHighLevelClient.class).toProvider(RestClientProvider.class).in(Singleton.class);
		bind(RestClientFactory.class).to(ElasticsearchRestClientFactory.class).in(Singleton.class);

		// Mapper and parser
		bind(ObjectMapper.class).toInstance(new ObjectMapper());
		bind(ResponseMapper.class).to(JsonContentResponseMapper.class).in(Singleton.class);
		bind(NodeInfoParser.class).to(DefaultNodeInfoParser.class).in(Singleton.class);
	}
}
