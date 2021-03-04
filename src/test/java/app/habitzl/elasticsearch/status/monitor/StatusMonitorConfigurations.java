package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;

/**
 * Utility class for creating random status monitor configuration.
 */
public final class StatusMonitorConfigurations {

    private static final String LOCAL_IP_ADDRESS = "127.0.0.1";
    private static final int MAX_PORT = 9999;

    private StatusMonitorConfigurations() {
        // instantiation protection
    }

    public static StatusMonitorConfiguration random() {
        int port = Randoms.generateInteger(MAX_PORT + 1);
        boolean isUsingHttps = Randoms.generateBoolean();

        StatusMonitorConfiguration configuration = new StatusMonitorConfiguration();
        configuration.setHost(LOCAL_IP_ADDRESS);
        configuration.setPort(Integer.toString(port));
        configuration.setUsingHttps(isUsingHttps);
        configuration.setUsername(Randoms.generateString("user-"));
        configuration.setPassword(Randoms.generateString("password-"));
        return configuration;
    }
}
