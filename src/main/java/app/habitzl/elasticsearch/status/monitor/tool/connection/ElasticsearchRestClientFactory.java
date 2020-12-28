package app.habitzl.elasticsearch.status.monitor.tool.connection;

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

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Optional;

public class ElasticsearchRestClientFactory implements RestClientFactory {
	private static final Logger LOG = LogManager.getLogger(ElasticsearchRestClientFactory.class);

	private static final boolean SECURITY_ENABLED = true;

	static final String DEFAULT_HOST = "localhost";
	static final int DEFAULT_PORT = 9200;
	static final String HTTP_SCHEME = "http";
	static final String HTTPS_SCHEME = "https";

	static final String DEFAULT_KEYSTORE_PASSWORD = "changeit";
	static final String DEFAULT_USER = "admin";
	static final String DEFAULT_PASSWORD = "admin";

	@Override
	public RestHighLevelClient create() {
		RestClientBuilder builder = RestClient.builder(createHttpHost());

		builder.setHttpClientConfigCallback(createHttpClientConfigCallback());

		return new RestHighLevelClient(builder);
	}

	private HttpHost createHttpHost() {
		return new HttpHost(
				DEFAULT_HOST,
				DEFAULT_PORT,
				SECURITY_ENABLED ? HTTPS_SCHEME : HTTP_SCHEME
		);
	}

	private RestClientBuilder.HttpClientConfigCallback createHttpClientConfigCallback() {
		HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();

		if (SECURITY_ENABLED) {
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
