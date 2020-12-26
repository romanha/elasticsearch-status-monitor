package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.connection.ElasticsearchRestClientFactory;
import app.habitzl.elasticsearch.status.monitor.connection.RestClientFactory;
import app.habitzl.elasticsearch.status.monitor.connection.RestClientProvider;
import app.habitzl.elasticsearch.status.monitor.mapper.FromLowLevelClientResponseMapper;
import app.habitzl.elasticsearch.status.monitor.mapper.FromMapNodeInfoParser;
import app.habitzl.elasticsearch.status.monitor.mapper.NodeInfoParser;
import app.habitzl.elasticsearch.status.monitor.mapper.ResponseMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public class GuiceModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(StatusMonitor.class).to(ElasticsearchStatusMonitor.class).in(Singleton.class);

		// REST client
		bind(RestHighLevelClient.class).toProvider(RestClientProvider.class).in(Singleton.class);
		bind(RestClientFactory.class).to(ElasticsearchRestClientFactory.class).in(Singleton.class);

		// Mapper and parser
		bind(ResponseMapper.class).to(FromLowLevelClientResponseMapper.class).in(Singleton.class);
		bind(NodeInfoParser.class).to(FromMapNodeInfoParser.class).in(Singleton.class);
	}
}
