package app.habitzl.elasticsearch.status.monitor.connection;

import org.elasticsearch.client.RestHighLevelClient;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public interface RestClientFactory {

	/**
	 * Creates a new Elasticsearch REST client.
	 */
	RestHighLevelClient create();
}
