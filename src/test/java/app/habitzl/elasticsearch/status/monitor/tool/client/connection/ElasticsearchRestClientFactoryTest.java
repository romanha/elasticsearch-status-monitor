package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
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
	private StatusMonitorConfiguration configuration;

	@BeforeEach
	void setUp() {
		configuration = StatusMonitorConfigurations.random();
		sut = new ElasticsearchRestClientFactory(configuration);
	}

	@Test
	void create_sut_returnsClient() {
		// When
		RestHighLevelClient client = sut.create();

		// Then
		assertThat(client, notNullValue());
	}

	@Test
	void create_sut_returnsClientWithConfiguredHttpHost() {
		// Given
		String configuredAddress = configuration.getIpAddress();
		int configuredPort = Integer.parseInt(configuration.getPort());
		String configuredScheme = configuration.isUsingHttps()
				? ElasticsearchRestClientFactory.HTTPS_SCHEME
				: ElasticsearchRestClientFactory.HTTP_SCHEME;

		// When
		RestHighLevelClient client = sut.create();

		// Then
		List<Node> nodes = client.getLowLevelClient().getNodes();
		assertThat(nodes, hasSize(1));

		HttpHost host = nodes.iterator().next().getHost();
		assertThat(host.getHostName(), equalTo(configuredAddress));
		assertThat(host.getPort(), equalTo(configuredPort));
		assertThat(host.getSchemeName(), equalTo(configuredScheme));
	}

	@Test
	void create_invalidConfiguration_returnsClientWithFallbackConfigurationHttpHost() {
		// Given
		configuration.setIpAddress("invalid#address");
		configuration.setPort("invalid#port");
		String fallbackAddress = ElasticsearchRestClientFactory.FALLBACK_HOST;
		int configuredPort = ElasticsearchRestClientFactory.FALLBACK_PORT;
		String configuredScheme = this.configuration.isUsingHttps()
				? ElasticsearchRestClientFactory.HTTPS_SCHEME
				: ElasticsearchRestClientFactory.HTTP_SCHEME;

		// When
		RestHighLevelClient client = sut.create();

		// Then
		List<Node> nodes = client.getLowLevelClient().getNodes();
		assertThat(nodes, hasSize(1));

		HttpHost host = nodes.iterator().next().getHost();
		assertThat(host.getHostName(), equalTo(fallbackAddress));
		assertThat(host.getPort(), equalTo(configuredPort));
		assertThat(host.getSchemeName(), equalTo(configuredScheme));
	}
}