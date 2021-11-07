package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;

import java.io.File;
import java.util.List;

/**
 * Utility class for creating random status monitor configuration.
 */
public final class StatusMonitorConfigurations {

    private StatusMonitorConfigurations() {
        // instantiation protection
    }

    public static StatusMonitorConfiguration random() {
        boolean isUsingHttps = Randoms.generateBoolean();

        StatusMonitorConfiguration configuration = new StatusMonitorConfiguration();
        configuration.setHost(Hosts.randomHostName());
        configuration.setPort(Integer.toString(Hosts.randomPort()));
        configuration.setFallbackEndpoints(randomFallbackEndpoints());
        configuration.setUsingHttps(isUsingHttps);
        configuration.setUsername(Randoms.generateString("user-"));
        configuration.setPassword(Randoms.generateString("password-"));
        configuration.setReportFilesPath(Randoms.generateString("reports" + File.separator));
        return configuration;
    }

    private static List<String> randomFallbackEndpoints() {
        return List.of(
                Hosts.randomAddress(),
                Hosts.randomAddress(),
                Hosts.randomAddress()
        );
    }
}
