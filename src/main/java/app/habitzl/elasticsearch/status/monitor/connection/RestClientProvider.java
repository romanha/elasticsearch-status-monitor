package app.habitzl.elasticsearch.status.monitor.connection;

import org.elasticsearch.client.RestHighLevelClient;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Objects;

/**
 * Created by Roman Habitzl on 26.12.2020.
 */
public class RestClientProvider implements Provider<RestHighLevelClient> {

	private RestHighLevelClient client;
	private final RestClientFactory factory;

	@Inject
	public RestClientProvider(final RestClientFactory factory) {
		this.factory = factory;
	}

	@Override
	public RestHighLevelClient get() {
		return Objects.nonNull(client) ? client : createClient();
	}

	private RestHighLevelClient createClient() {
		client = factory.create();
		return client;
	}
}
