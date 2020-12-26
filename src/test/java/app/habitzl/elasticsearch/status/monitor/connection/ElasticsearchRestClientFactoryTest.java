package app.habitzl.elasticsearch.status.monitor.connection;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class ElasticsearchRestClientFactoryTest {

	private ElasticsearchRestClientFactory sut;

	@BeforeEach
	void setUp() {
		sut = new ElasticsearchRestClientFactory();
	}

	@Test
	void create_sut_returnsClient() {
		// When
		RestHighLevelClient client = sut.create();

		// Then
		assertThat(client, notNullValue());
	}
}