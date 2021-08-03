package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.Iterator;
import java.util.List;

import static app.habitzl.elasticsearch.status.monitor.tool.client.connection.FallbackElasticsearchRestClientFactory.HOST_PORT_SEPARATOR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class FallbackElasticsearchRestClientFactoryTest {

    private FallbackElasticsearchRestClientFactory sut;
    private StatusMonitorConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = StatusMonitorConfigurations.random();
        sut = new FallbackElasticsearchRestClientFactory(configuration);
    }

    @Test
    void create_noFallbackEndpointsConfigured_returnsNoClients() {
        // Given
        configuration.setFallbackEndpoints(List.of());

        // When
        List<RestClient> clients = sut.create();

        // Then
        assertThat(clients, empty());
    }

    @Test
    void create_fallbackEndpointsConfigured_returnsClients() {
        // Given
        List<String> fallbackEndpoints = List.of(
                "localhost:9200",
                "localhost:9202",
                "localhost:9204"
        );
        configuration.setFallbackEndpoints(fallbackEndpoints);

        // When
        List<RestClient> clients = sut.create();

        // Then
        assertThat(clients, hasSize(fallbackEndpoints.size()));
    }

    @Test
    void create_fallbackEndpointsConfigured_returnsValidClientsWithConfiguredHttpHost() {
        // Given
        String localhost = "localhost";
        int port1 = 9202;
        String localhostAddress = "localhost";
        int port2 = 9218;
        String invalidEndpoint1 = "invalid#host:9200";
        String invalidEndpoint2 = "localhost:invalid#port";
        String invalidEndpoint3 = "localhost:9200:9202";
        String invalidEndpoint4 = "localhost";
        List<String> fallbackEndpoints = List.of(
                localhost + HOST_PORT_SEPARATOR + port1,
                localhostAddress + HOST_PORT_SEPARATOR + port2,
                invalidEndpoint1,
                invalidEndpoint2,
                invalidEndpoint3,
                invalidEndpoint4
        );
        configuration.setFallbackEndpoints(fallbackEndpoints);
        String configuredScheme = configuration.isUsingHttps()
                ? ElasticsearchRestClientFactory.HTTPS_SCHEME
                : ElasticsearchRestClientFactory.HTTP_SCHEME;

        // When
        List<RestClient> clients = sut.create();

        // Then
        assertThat(clients, hasSize(2));
        Iterator<RestClient> iterator = clients.iterator();

        assertClient(iterator.next(), localhost, port1, configuredScheme);
        assertClient(iterator.next(), localhostAddress, port2, configuredScheme);
    }

    @EnabledOnOs(OS.WINDOWS)
    @Test
    void create_windowsEnvironmentAndFallbackEndpointConfiguredWithLocalHostAddress_returnsClientWithLocalHostAddress() {
        // Given
        String localhostAddress = "127.0.0.1";
        String expectedHostName = "127.0.0.1";
        int port = 9280;
        List<String> fallbackEndpoints = List.of(localhostAddress + HOST_PORT_SEPARATOR + port);
        configuration.setFallbackEndpoints(fallbackEndpoints);
        String configuredScheme = configuration.isUsingHttps()
                ? ElasticsearchRestClientFactory.HTTPS_SCHEME
                : ElasticsearchRestClientFactory.HTTP_SCHEME;

        // When
        List<RestClient> clients = sut.create();

        // Then
        assertThat(clients, hasSize(1));
        Iterator<RestClient> iterator = clients.iterator();
        assertClient(iterator.next(), expectedHostName, port, configuredScheme);
    }

    @EnabledOnOs(OS.LINUX)
    @Test
    void create_linuxEnvironmentAndFallbackEndpointConfiguredWithLocalHostAddress_returnsClientWithLocalHostName() {
        // Given
        String localhostAddress = "127.0.0.1";
        String expectedHostName = "localhost";
        int port = 9280;
        List<String> fallbackEndpoints = List.of(localhostAddress + HOST_PORT_SEPARATOR + port);
        configuration.setFallbackEndpoints(fallbackEndpoints);
        String configuredScheme = configuration.isUsingHttps()
                ? ElasticsearchRestClientFactory.HTTPS_SCHEME
                : ElasticsearchRestClientFactory.HTTP_SCHEME;

        // When
        List<RestClient> clients = sut.create();

        // Then
        assertThat(clients, hasSize(1));
        Iterator<RestClient> iterator = clients.iterator();
        assertClient(iterator.next(), expectedHostName, port, configuredScheme);
    }

    private void assertClient(final RestClient client, final String expectedHost, final int expectedPort, final String expectedScheme) {
        List<Node> nodes = client.getNodes();
        assertThat(nodes, hasSize(1));
        HttpHost host = nodes.iterator().next().getHost();
        assertThat(host.getHostName(), equalTo(expectedHost));
        assertThat(host.getPort(), equalTo(expectedPort));
        assertThat(host.getSchemeName(), equalTo(expectedScheme));
    }
}