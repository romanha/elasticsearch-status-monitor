package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;

import java.security.SecureRandom;

/**
 * Utility class for creating random status monitor configuration.
 */
public final class StatusMonitorConfigurations {

    private static final int ONE_HUNDRED = 100;
    private static final String LOCAL_IP_ADDRESS = "127.0.0.1";
    private static final int MAX_PORT = 9999;

    private StatusMonitorConfigurations() {
        // instantiation protection
    }

    public static StatusMonitorConfiguration random() {
        SecureRandom random = new SecureRandom();

        int port = random.nextInt(MAX_PORT + 1);
        boolean isUsingHttps = random.nextBoolean();

        StatusMonitorConfiguration configuration = new StatusMonitorConfiguration();
        configuration.setHost(LOCAL_IP_ADDRESS);
        configuration.setPort(Integer.toString(port));
        configuration.setUsingHttps(isUsingHttps);
        configuration.setUsername("user-" + random.nextInt(ONE_HUNDRED));
        configuration.setPassword("password-" + random.nextInt(ONE_HUNDRED));
        return configuration;
    }
}
