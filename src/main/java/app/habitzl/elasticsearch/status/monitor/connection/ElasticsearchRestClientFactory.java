package app.habitzl.elasticsearch.status.monitor.connection;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public class ElasticsearchRestClientFactory implements RestClientFactory {

	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 9200;
	public static final String DEFAULT_SCHEME = "http";

	@Override
	public RestHighLevelClient create() {
		RestClientBuilder builder = RestClient.builder(createHttpHost());
		return new RestHighLevelClient(builder);
	}

	private HttpHost createHttpHost() {
		return new HttpHost(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_SCHEME);
	}
}
