package app.habitzl.elasticsearch.status.monitor.tool.connection;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticsearchRestClientFactory implements RestClientFactory {

	static final String DEFAULT_HOST = "localhost";
	static final int DEFAULT_PORT = 9200;
	static final String DEFAULT_SCHEME = "http";

	@Override
	public RestHighLevelClient create() {
		RestClientBuilder builder = RestClient.builder(createHttpHost());
		return new RestHighLevelClient(builder);
	}

	private HttpHost createHttpHost() {
		return new HttpHost(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_SCHEME);
	}
}
