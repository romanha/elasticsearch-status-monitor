package app.habitzl.elasticsearch.status.monitor;

import org.apache.http.HttpHost;

/**
 * Utility class for creating random host data.
 */
public final class Hosts {

    private static final String HTTPS_SCHEME = "https";
    private static final int PORT_MINIMUM = 9200;
    private static final int PORT_MAXIMUM = 9300;

    private Hosts() {
        // instantiation protection
    }

    public static HttpHost randomHttpHost() {
        return new HttpHost(randomHostName(), randomPort(), HTTPS_SCHEME);
    }

    /**
     * Returns a random URI, e.g. {@code https://host-test:9200}.
     */
    public static String randomUri() {
        return randomHttpHost().toURI();
    }

    /**
     * Returns a random address, e.g. {@code host-test:9200}.
     */
    public static String randomAddress() {
        return randomHttpHost().toHostString();
    }

    /**
     * Returns a random host name, e.g. {@code host-test}.
     */
    public static String randomHostName() {
        return Randoms.generateString("host-");
    }

    /**
     * Returns a random host port, e.g. {@code 9200}.
     */
    public static int randomPort() {
        return Randoms.generateInteger(PORT_MINIMUM, PORT_MAXIMUM);
    }
}
