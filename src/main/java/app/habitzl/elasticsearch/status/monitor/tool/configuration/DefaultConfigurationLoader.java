package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import app.habitzl.elasticsearch.status.monitor.AnalysisStartOption;
import app.habitzl.elasticsearch.status.monitor.ConfigurationLoader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import javax.inject.Inject;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultConfigurationLoader implements ConfigurationLoader {
    private static final Logger LOG = LogManager.getLogger(DefaultConfigurationLoader.class);

    private final StatusMonitorConfiguration configuration;
    private final CliOptions cliOptions;

    @Inject
    public DefaultConfigurationLoader(final StatusMonitorConfiguration configuration, final CliOptions cliOptions) {
        this.configuration = configuration;
        this.cliOptions = cliOptions;
    }

    @Override
    public AnalysisStartOption load(final String[] cliArguments) {
        return parseCliOptions(cliArguments);
    }

    private AnalysisStartOption parseCliOptions(final String[] cliArguments) {
        AnalysisStartOption startOption;

        DefaultParser cliParser = new DefaultParser();
        try {
            CommandLine commandLine = cliParser.parse(cliOptions.getAvailableOptions(), cliArguments);
            parseConnectionOptions(commandLine);
            parseAuthenticationOptions(commandLine);
            parseOtherOptions(commandLine);
            startOption = parseHelpOptions(commandLine);
        } catch (final ParseException e) {
            LOG.error("Invalid options provided for starting the Elasticsearch Status Monitor. {}", e.getMessage());
            LOG.info("Falling back to default configuration.");
            startOption = AnalysisStartOption.ANALYSIS_NOT_POSSIBLE;
        }

        return startOption;
    }

    private void parseConnectionOptions(final CommandLine commandLine) {
        if (commandLine.hasOption(CliOptions.HOST_OPTION_SHORT)) {
            LOG.info("Using host '{}' from CLI options.", commandLine.getOptionValue(CliOptions.HOST_OPTION_SHORT));
            configuration.setHost(commandLine.getOptionValue(CliOptions.HOST_OPTION_SHORT));
        }

        if (commandLine.hasOption(CliOptions.PORT_OPTION_SHORT)) {
            LOG.info("Using port '{}' from CLI options.", commandLine.getOptionValue(CliOptions.PORT_OPTION_SHORT));
            configuration.setPort(commandLine.getOptionValue(CliOptions.PORT_OPTION_SHORT));
        }

        if (commandLine.hasOption(CliOptions.FALLBACK_ENDPOINTS_OPTION_LONG)) {
            String fallbackEndpointsOption = commandLine.getOptionValue(CliOptions.FALLBACK_ENDPOINTS_OPTION_LONG);
            List<String> fallbackEndpoints = Arrays.stream(fallbackEndpointsOption.split(","))
                    .map(String::trim)
                    .filter(Strings::isNotBlank)
                    .collect(Collectors.toList());
            LOG.info("Using fallback endpoints '{}' from CLI options.", fallbackEndpointsOption);
            configuration.setFallbackEndpoints(fallbackEndpoints);
        }

        if (commandLine.hasOption(CliOptions.UNSECURE_OPTION_LONG)) {
            LOG.info("Security is disabled by CLI options.");
            configuration.setUsingHttps(false);
        }
    }

    private void parseAuthenticationOptions(final CommandLine commandLine) {
        if (commandLine.hasOption(CliOptions.USER_OPTION_LONG)) {
            LOG.info("Using user name '{}' from CLI options.", commandLine.getOptionValue(CliOptions.USER_OPTION_LONG));
            configuration.setUsername(commandLine.getOptionValue(CliOptions.USER_OPTION_LONG));
        }

        if (commandLine.hasOption(CliOptions.PASSWORD_OPTION_LONG)) {
            LOG.info("Using password from CLI options.");
            configuration.setPassword(commandLine.getOptionValue(CliOptions.PASSWORD_OPTION_LONG));
        }
    }

    private void parseOtherOptions(final CommandLine commandLine) {
        if (commandLine.hasOption(CliOptions.REPORT_FILES_PATH_OPTION_LONG)) {
            String reportFilesPath = commandLine.getOptionValue(CliOptions.REPORT_FILES_PATH_OPTION_LONG);
            try {
                Paths.get(reportFilesPath);
                LOG.info("Using report file location '{}' from CLI options.", reportFilesPath);
                configuration.setReportFilesPath(reportFilesPath);
            } catch (final InvalidPathException e) {
                LOG.warn("The provided report files path '{}' is no valid path. Falling back to the default path.", reportFilesPath);
            }
        }
    }

    /**
     * Parse all help options that can cause the analysis tool to not start.
     * Prints requested output (e.g. help, version) and returns the concluded start option.
     */
    private AnalysisStartOption parseHelpOptions(final CommandLine commandLine) {
        AnalysisStartOption startOption = AnalysisStartOption.ANALYSIS_POSSIBLE;

        if (commandLine.hasOption(CliOptions.VERSION_OPTION_LONG)) {
            printVersion();
            startOption = AnalysisStartOption.ANALYSIS_NOT_REQUESTED;
        }

        if (commandLine.hasOption(CliOptions.HELP_OPTION_LONG)) {
            printHelp();
            startOption = AnalysisStartOption.ANALYSIS_NOT_REQUESTED;
        }

        return startOption;
    }

    /**
     * Requires the version to be in the JAR's manifest file.
     * This is added by using the maven-assembly-plugin configuration options
     * {@code addDefaultImplementationEntries} and {@code addDefaultSpecificationEntries}.
     */
    private void printVersion() {
        String version = getClass().getPackage().getImplementationVersion();
        LOG.debug("Version: {}", version);
    }

    private void printHelp() {
        String cmdLineSyntax = "java -jar elasticsearch-status-monitor.jar";
        String header = "This tool provides a quick overview of an Elasticsearch cluster and analyses the data for potential problems.";
        String footer = "Please report issues at: https://github.com/romanha/elasticsearch-status-monitor/issues";

        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(cmdLineSyntax, header, cliOptions.getAvailableOptions(), footer, true);
    }
}
