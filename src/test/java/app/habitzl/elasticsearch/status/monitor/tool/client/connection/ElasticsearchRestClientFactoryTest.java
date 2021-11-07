package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import app.habitzl.elasticsearch.status.monitor.Hosts;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ElasticsearchRestClientFactoryTest {

    private static final String LOCALHOST = "localhost";

    private ElasticsearchRestClientFactory sut;
    private StatusMonitorConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = configurationWithoutFallbackEndpoints();
        sut = new ElasticsearchRestClientFactory(configuration);
    }

    @Test
    void create_sut_returnsClient() {
        // When
        RestClient client = sut.create();

        // Then
        assertThat(client, notNullValue());
    }

    @Test
    void create_invalidHostNameAndPort_returnsClientWithDefaultHostNameAndDefaultPort() {
        // Given
        configuration.setHost("invalid#host");
        configuration.setPort("invalid#port");
        String configuredScheme = getConfiguredScheme();
        String defaultHostName = ElasticsearchRestClientFactory.DEFAULT_HOST;
        int defaultPort = ElasticsearchRestClientFactory.DEFAULT_PORT;

        // When
        RestClient client = sut.create();

        // Then
        List<Node> nodes = client.getNodes();
        assertThat(nodes, hasSize(1));
        HttpHost host = nodes.iterator().next().getHost();
        assertHost(host, configuredScheme, defaultHostName, defaultPort);
    }

    @Test
    void create_invalidHostName_returnsClientWithDefaultHttpHostNameAndConfiguredPort() {
        // Given
        int configuredPort = Hosts.randomPort();
        configuration.setHost("invalid#host");
        configuration.setPort(Integer.toString(configuredPort));
        String configuredScheme = getConfiguredScheme();
        String defaultHostName = ElasticsearchRestClientFactory.DEFAULT_HOST;

        // When
        RestClient client = sut.create();

        // Then
        List<Node> nodes = client.getNodes();
        assertThat(nodes, hasSize(1));
        HttpHost host = nodes.iterator().next().getHost();
        assertHost(host, configuredScheme, defaultHostName, configuredPort);
    }

    @Test
    void create_withKnownEndpointAndWithoutFallbackEndpoints_returnsClientWithExpectedHttpHost() {
        // Given
        String configuredScheme = getConfiguredScheme();
        int configuredPort = Integer.parseInt(configuration.getPort());

        // When
        RestClient client = sut.create();

        // Then
        List<Node> nodes = client.getNodes();
        assertThat(nodes, hasSize(1));
        assertLocalHost(nodes.iterator().next().getHost(), configuredScheme, configuredPort);
    }

    @Test
    void create_withKnownEndpointAndInvalidFallbackEndpoints_returnsClientWithExpectedHttpHost() {
        // Given
        configuration.setFallbackEndpoints(List.of(
                createInvalidEndpointAddress(),
                createInvalidEndpointAddress())
        );
        String configuredScheme = getConfiguredScheme();
        int configuredPort = Integer.parseInt(configuration.getPort());

        // When
        RestClient client = sut.create();

        // Then
        List<Node> nodes = client.getNodes();
        assertThat(nodes, hasSize(1));
        assertLocalHost(nodes.iterator().next().getHost(), configuredScheme, configuredPort);
    }

    @Test
    void create_withKnownEndpointAndUnknownFallbackEndpoints_returnsClientWithExpectedHttpHost() {
        // Given
        configuration.setFallbackEndpoints(List.of(
                createUnknownEndpointAddress(),
                createUnknownEndpointAddress())
        );
        String configuredScheme = getConfiguredScheme();
        int configuredPort = Integer.parseInt(configuration.getPort());

        // When
        RestClient client = sut.create();

        // Then
        List<Node> nodes = client.getNodes();
        assertThat(nodes, hasSize(1));
        assertLocalHost(nodes.iterator().next().getHost(), configuredScheme, configuredPort);
    }

    @Test
    void create_withKnownEndpointAndKnownFallbackEndpoints_returnsClientWithAllExpectedHttpHosts() {
        // Given
        int fallbackPort1 = Hosts.randomPort();
        int fallbackPort2 = Hosts.randomPort();
        configuration.setFallbackEndpoints(List.of(
                createEndpointAddress(fallbackPort1),
                createEndpointAddress(fallbackPort2))
        );
        String configuredScheme = getConfiguredScheme();
        int configuredPort = Integer.parseInt(configuration.getPort());

        // When
        RestClient client = sut.create();

        // Then
        List<Node> nodes = client.getNodes();
        assertThat(nodes, hasSize(3));
        Iterator<Node> iterator = nodes.iterator();
        assertLocalHost(iterator.next().getHost(), configuredScheme, configuredPort);
        assertLocalHost(iterator.next().getHost(), configuredScheme, fallbackPort1);
        assertLocalHost(iterator.next().getHost(), configuredScheme, fallbackPort2);
    }

    @EnabledOnOs(OS.WINDOWS)
    @Test
    void create_windowsEnvironmentAndEndpointConfiguredWithLocalHostAddress_returnsClientWithLocalHostAddress() {
        // Given
        String localhostAddress = "127.0.0.1";
        String expectedHostName = "127.0.0.1";
        configuration.setHost(localhostAddress);
        String configuredScheme = getConfiguredScheme();
        int configuredPort = Integer.parseInt(configuration.getPort());

        // When
        RestClient client = sut.create();

        // Then
        List<Node> nodes = client.getNodes();
        assertThat(nodes, hasSize(1));
        HttpHost host = nodes.iterator().next().getHost();
        assertHost(host, configuredScheme, expectedHostName, configuredPort);
    }

    @EnabledOnOs(OS.LINUX)
    @Test
    void create_linuxEnvironmentAndEndpointConfiguredWithLocalHostAddress_returnsClientWithLocalHostName() {
        // Given
        String localhostAddress = "127.0.0.1";
        String expectedHostName = "localhost";
        configuration.setHost(localhostAddress);
        String configuredScheme = getConfiguredScheme();
        int configuredPort = Integer.parseInt(configuration.getPort());

        // When
        RestClient client = sut.create();

        // Then
        List<Node> nodes = client.getNodes();
        assertThat(nodes, hasSize(1));
        HttpHost host = nodes.iterator().next().getHost();
        assertHost(host, configuredScheme, expectedHostName, configuredPort);
    }

    private StatusMonitorConfiguration configurationWithoutFallbackEndpoints() {
        StatusMonitorConfiguration config = StatusMonitorConfigurations.random();
        config.setHost(LOCALHOST);
        config.setFallbackEndpoints(List.of());
        return config;
    }

    private String createEndpointAddress(final int port) {
        return LOCALHOST + ElasticsearchRestClientFactory.HOST_PORT_SEPARATOR + port;
    }

    private String createUnknownEndpointAddress() {
        return Hosts.randomAddress();
    }

    private String createInvalidEndpointAddress() {
        return Hosts.randomHostName() + "-without-port";
    }

    private String getConfiguredScheme() {
        return configuration.isUsingHttps()
                ? ElasticsearchRestClientFactory.HTTPS_SCHEME
                : ElasticsearchRestClientFactory.HTTP_SCHEME;
    }

    private void assertLocalHost(final HttpHost actualHost, final String expectedScheme, final int expectedPort) {
        assertHost(actualHost, expectedScheme, LOCALHOST, expectedPort);
    }

    private void assertHost(
            final HttpHost actualHost,
            final String expectedScheme,
            final String expectedHostName,
            final int expectedPort) {
        assertThat(actualHost.getSchemeName(), equalTo(expectedScheme));
        assertThat(actualHost.getHostName(), equalTo(expectedHostName));
        assertThat(actualHost.getPort(), equalTo(expectedPort));
    }
}