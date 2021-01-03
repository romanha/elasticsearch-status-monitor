package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Optional;

public class ElasticsearchRestClientFactory implements RestClientFactory {
	private static final Logger LOG = LogManager.getLogger(ElasticsearchRestClientFactory.class);

	static final String FALLBACK_HOST = "localhost";
	static final int FALLBACK_PORT = 9200;
	static final String HTTP_SCHEME = "http";
	static final String HTTPS_SCHEME = "https";

	static final String DEFAULT_KEYSTORE_PASSWORD = "changeit";
	static final String DEFAULT_USER = "admin";
	static final String DEFAULT_PASSWORD = "admin";

	private final StatusMonitorConfiguration configuration;

	@Inject
	public ElasticsearchRestClientFactory(final StatusMonitorConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public RestHighLevelClient create() {
		RestClientBuilder builder = RestClient.builder(createHttpHost());

		builder.setHttpClientConfigCallback(createHttpClientConfigCallback());

		return new RestHighLevelClient(builder);
	}

	private HttpHost createHttpHost() {
		HttpHost host;

		String scheme = configuration.isUsingHttps() ? HTTPS_SCHEME : HTTP_SCHEME;

		try {
			InetAddress address = InetAddress.getByName(configuration.getIpAddress());
			host = new HttpHost(address, getConfiguredPort(), scheme);
		} catch (final UnknownHostException e) {
			LOG.error("Failed to create IP address from '" + configuration.getIpAddress() + "'.", e);
			LOG.info("Falling back to '{}'.", FALLBACK_HOST);
			host = new HttpHost(FALLBACK_HOST, getConfiguredPort(), scheme);
		}

		return host;
	}

	private int getConfiguredPort() {
		int port;
		try {
			port = Integer.parseInt(configuration.getPort());
		} catch (final NumberFormatException e) {
			LOG.error("Failed to parse port from '" + configuration.getPort() + "'.", e);
			LOG.info("Falling back to '{}'.", FALLBACK_PORT);
			port = FALLBACK_PORT;
		}

		return port;
	}

	private RestClientBuilder.HttpClientConfigCallback createHttpClientConfigCallback() {
		HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();

		if (configuration.isUsingHttps()) {
			setCredentialsProvider(builder);
			setSSLContext(builder);
		}

		return callback -> builder;
	}

	private void setCredentialsProvider(final HttpAsyncClientBuilder builder) {
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(DEFAULT_USER, DEFAULT_PASSWORD);
		credentialsProvider.setCredentials(AuthScope.ANY, credentials);

		builder.setDefaultCredentialsProvider(credentialsProvider);
	}

	private void setSSLContext(final HttpAsyncClientBuilder builder) {
		Optional<SSLContext> sslContext = getSslContext();
		sslContext.ifPresent(builder::setSSLContext);
	}

	/**
	 * Get the SSL context from the default Java KeyStore.
	 */
	private Optional<SSLContext> getSslContext() {
		SSLContext sslContext = null;
		try {
			KeyStore truststore = KeyStore.getInstance("jks");

			Path keyStorePath = getDefaultKeystorePath();
			try (InputStream is = Files.newInputStream(keyStorePath)) {
				truststore.load(is, DEFAULT_KEYSTORE_PASSWORD.toCharArray());
				LOG.info("Loaded the Java KeyStore from the path {}.", keyStorePath);
			}

			sslContext = SSLContexts.custom()
									.loadTrustMaterial(truststore, null)
									.build();
		} catch (final Exception e) {
			LOG.error("Failed to create SSL context.", e);
		}

		return Optional.ofNullable(sslContext);
	}

	/**
	 * Gets the default keystore path located at {@code %JAVA_HOME%/lib/security/cacerts}.
	 */
	private Path getDefaultKeystorePath() {
		return Paths.get(
				System.getProperties().getProperty("java.home") + File.separator
						+ "lib" + File.separator
						+ "security" + File.separator
						+ "cacerts"
		);
	}
}
