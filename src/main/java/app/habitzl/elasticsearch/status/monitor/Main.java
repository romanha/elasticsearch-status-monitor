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

        teardown(injector);
        exit(injector, startOption, report);
    }

    private static AnalysisReport performAnalysis(final Injector injector) {
        StatusMonitor statusMonitor = injector.getInstance(StatusMonitor.class);
        return statusMonitor.createSnapshot();
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

    private static void exit(final Injector injector, final AnalysisStartOption startOption, final AnalysisReport report) {
        ExitCodeMapper exitCodeMapper = injector.getInstance(ExitCodeMapper.class);
        ExitCode exitCode = exitCodeMapper.getExitCode(startOption, report);
        LOG.info("Exiting program with code '{}' ({}).", exitCode, exitCode.value());
        System.exit(exitCode.value());
    }
}
