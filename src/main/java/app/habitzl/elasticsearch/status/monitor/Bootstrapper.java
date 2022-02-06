package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import app.habitzl.elasticsearch.status.monitor.tool.client.connection.MonitoringRestClient;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

/**
 * The bootstrapper is responsible for
 * <ul>
 *     <li>initialising all components,</li>
 *     <li>loading the configuration,</li>
 *     <li>starting the analysis,</li>
 *     <li>tearing down the application,</li>
 *     <li>and returning the exit code.</li>
 * </ul>
 */
public class Bootstrapper {
    private static final Logger LOG = LogManager.getLogger(Bootstrapper.class);

    /**
     * This method is the single entry point for starting the application.
     *
     * @return the exit code of the application
     */
    public static int start(final String[] arguments) {
        LOG.info("Starting up the Elasticsearch Status Monitor.");

        Injector injector = Guice.createInjector(new GuiceModule());

        ConfigurationLoader configurationLoader = injector.getInstance(ConfigurationLoader.class);
        AnalysisStartOption startOption = configurationLoader.load(arguments);
        AnalysisReport report = startAnalysis(injector, startOption);

        teardown(injector);
        return getExitCodeValue(injector, startOption, report);
    }

    private static AnalysisReport startAnalysis(final Injector injector, final AnalysisStartOption startOption) {
        AnalysisReport report;
        switch (startOption) {
            case ANALYSIS_POSSIBLE:
                report = performAnalysis(injector);
                break;
            case ANALYSIS_NOT_REQUESTED:
                report = null;
                LOG.info("Analysis start not requested due to the use of help options.");
                break;
            case ANALYSIS_NOT_POSSIBLE:
            default:
                report = null;
                LOG.error("Analysis start not possible due to misconfiguration.");
                break;
        }

        return report;
    }

    private static AnalysisReport performAnalysis(final Injector injector) {
        StatusMonitor statusMonitor = injector.getInstance(StatusMonitor.class);
        return statusMonitor.createSnapshot();
    }

    private static void teardown(final Injector injector) {
        LOG.debug("Closing Elasticsearch Status Monitor.");
        closeMonitoringRestClient(injector);
    }

    private static void closeMonitoringRestClient(final Injector injector) {
        Key<RestClient> restClientKey = Key.get(RestClient.class, MonitoringRestClient.class);
        RestClient client = injector.getInstance(restClientKey);
        try {
            LOG.debug("Closing monitoring REST client.");
            client.close();
        } catch (final IOException e) {
            LOG.warn("Could not safely close the connection to the ES cluster.", e);
        }
    }

    private static int getExitCodeValue(final Injector injector, final AnalysisStartOption startOption, final AnalysisReport report) {
        ExitCodeMapper exitCodeMapper = injector.getInstance(ExitCodeMapper.class);
        ExitCode exitCode = exitCodeMapper.getExitCode(startOption, report);
        LOG.info("Exiting program with code '{}' ({}).", exitCode, exitCode.value());
        return exitCode.value();
    }
}
