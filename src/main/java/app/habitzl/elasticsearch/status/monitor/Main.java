package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * The tool's main entry point.
 */
public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(final String[] args) {
        LOG.info("Starting up the Elasticsearch Status Monitor.");

        Injector injector = Guice.createInjector(new GuiceModule());

        ConfigurationLoader configurationLoader = injector.getInstance(ConfigurationLoader.class);
        AnalysisStartOption startOption = configurationLoader.load(args);

        int exitCode;

        switch (startOption) {
            case ANALYSIS_POSSIBLE:
                exitCode = performAnalysis(injector);
                break;
            case ANALYSIS_NOT_REQUESTED:
                LOG.debug("Analysis start not requested due to the use of help options.");
                exitCode = 0;
                break;
            case ANALYSIS_NOT_POSSIBLE:
            default:
                LOG.error("Analysis start not possible due to misconfiguration.");
                exitCode = 3;
                break;
        }

        teardown(injector);
        System.exit(exitCode);
    }

    private static int performAnalysis(final Injector injector) {
        StatusMonitor statusMonitor = injector.getInstance(StatusMonitor.class);
        AnalysisReport report = statusMonitor.createSnapshot();
        return report.getMonitoringResult().getExitCode();
    }

    private static void teardown(final Injector injector) {
        LOG.info("Closing Elasticsearch Status Monitor.");
        closeElasticsearchRestClient(injector);
    }

    private static void closeElasticsearchRestClient(final Injector injector) {
        RestHighLevelClient client = injector.getInstance(RestHighLevelClient.class);
        try {
            client.close();
        } catch (final IOException e) {
            LOG.warn("Could not safely close the connection to the ES cluster.", e);
        }
    }
}
