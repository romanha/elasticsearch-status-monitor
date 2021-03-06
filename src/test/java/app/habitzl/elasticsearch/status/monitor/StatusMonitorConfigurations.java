package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;

import java.io.File;

/**
 * Utility class for creating random status monitor configuration.
 */
public final class StatusMonitorConfigurations {

    private static final int MAX_PORT = 9999;

    private StatusMonitorConfigurations() {
        // instantiation protection
    }

    public static StatusMonitorConfiguration random() {
        int port = Randoms.generateInteger(MAX_PORT + 1);
        boolean isUsingHttps = Randoms.generateBoolean();

        StatusMonitorConfiguration configuration = new StatusMonitorConfiguration();
        configuration.setHost(Randoms.generateString("host-"));
        configuration.setPort(Integer.toString(port));
        configuration.setUsingHttps(isUsingHttps);
        configuration.setUsername(Randoms.generateString("user-"));
        configuration.setPassword(Randoms.generateString("password-"));
        configuration.setReportFilesPath(Randoms.generateString("reports" + File.separator));
        return configuration;
    }
}
