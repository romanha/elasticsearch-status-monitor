package app.habitzl.elasticsearch.status.monitor.tool.connection;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

	@Test
	void create_sut_returnsClientWithDefaultHttpHost() {
		// Given
		// no custom connection defined

		// When
		RestHighLevelClient client = sut.create();

		// Then
		List<Node> nodes = client.getLowLevelClient().getNodes();
		assertThat(nodes, hasSize(1));

		HttpHost host = nodes.iterator().next().getHost();
		assertThat(host.getHostName(), equalTo(ElasticsearchRestClientFactory.DEFAULT_HOST));
		assertThat(host.getPort(), equalTo(ElasticsearchRestClientFactory.DEFAULT_PORT));
		assertThat(host.getSchemeName(), equalTo(ElasticsearchRestClientFactory.DEFAULT_SCHEME));
	}
}