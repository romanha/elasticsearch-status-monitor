package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;

import java.io.File;
import java.util.List;

/**
 * Utility class for creating random status monitor configuration.
 */
public final class StatusMonitorConfigurations {

    private static final int MAX_PORT = 9999;
    private static final String ENDPOINT_HOST_PORT_SEPARATOR = ":";

    private StatusMonitorConfigurations() {
        // instantiation protection
    }

    public static StatusMonitorConfiguration random() {
        boolean isUsingHttps = Randoms.generateBoolean();

        StatusMonitorConfiguration configuration = new StatusMonitorConfiguration();
        configuration.setHost(randomHost());
        configuration.setPort(randomPort());
        configuration.setFallbackEndpoints(randomFallbackEndpoints());
        configuration.setUsingHttps(isUsingHttps);
        configuration.setUsername(Randoms.generateString("user-"));
        configuration.setPassword(Randoms.generateString("password-"));
        configuration.setReportFilesPath(Randoms.generateString("reports" + File.separator));
        return configuration;
    }

    private static String randomHost() {
        return Randoms.generateString("host-");
    }

    private static String randomPort() {
        int port = Randoms.generateInteger(MAX_PORT);
        return Integer.toString(port);
    }

    private static List<String> randomFallbackEndpoints() {
        return List.of(
                randomHost() + ENDPOINT_HOST_PORT_SEPARATOR + randomPort(),
                randomHost() + ENDPOINT_HOST_PORT_SEPARATOR + randomPort(),
                randomHost() + ENDPOINT_HOST_PORT_SEPARATOR + randomPort()
        );
    }
}
