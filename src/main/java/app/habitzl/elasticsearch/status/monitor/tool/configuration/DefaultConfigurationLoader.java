package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import app.habitzl.elasticsearch.status.monitor.AnalysisStartOption;
import app.habitzl.elasticsearch.status.monitor.ConfigurationLoader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

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
            startOption = parseHelpOptions(commandLine);
        } catch (final ParseException e) {
            LOG.error("Invalid options provided for starting the Elasticsearch Status Monitor. Using default configuration.", e);
            startOption = AnalysisStartOption.ANALYSIS_NOT_POSSIBLE;
        }

        return startOption;
    }

    private void parseConnectionOptions(final CommandLine commandLine) {
        if (commandLine.hasOption(CliOptions.ADDRESS_OPTION)) {
            LOG.info("Using host {} from CLI options.", commandLine.getOptionValue(CliOptions.ADDRESS_OPTION));
            configuration.setHost(commandLine.getOptionValue(CliOptions.ADDRESS_OPTION));
        }

        if (commandLine.hasOption(CliOptions.PORT_OPTION)) {
            LOG.info("Using configured port {} from CLI options.", commandLine.getOptionValue(CliOptions.PORT_OPTION));
            configuration.setPort(commandLine.getOptionValue(CliOptions.PORT_OPTION));
        }

        if (commandLine.hasOption(CliOptions.SECURITY_OPTION)) {
            boolean isSecurityDisabled = commandLine.getOptionValue(CliOptions.SECURITY_OPTION).equalsIgnoreCase("false");
            LOG.info("Security is {} by CLI options.", isSecurityDisabled ? "disabled" : "enabled");
            configuration.setUsingHttps(!isSecurityDisabled);
        }
    }

    private void parseAuthenticationOptions(final CommandLine commandLine) {
        if (commandLine.hasOption(CliOptions.USER_OPTION)) {
            LOG.info("Using configured user name {} from CLI options.", commandLine.getOptionValue(CliOptions.USER_OPTION));
            configuration.setUsername(commandLine.getOptionValue(CliOptions.USER_OPTION));
        }

        if (commandLine.hasOption(CliOptions.PASSWORD_OPTION)) {
            LOG.info("Using configured password from CLI options.");
            configuration.setPassword(commandLine.getOptionValue(CliOptions.PASSWORD_OPTION));
        }
    }

    /**
     * Parse all help options that can cause the analysis tool to not start.
     * Prints requested output (e.g. help, version) and returns the concluded start option.
     */
    private AnalysisStartOption parseHelpOptions(final CommandLine commandLine) {
        AnalysisStartOption startOption = AnalysisStartOption.ANALYSIS_POSSIBLE;

        if (commandLine.hasOption(CliOptions.VERSION_OPTION)) {
            printVersion();
            startOption = AnalysisStartOption.ANALYSIS_NOT_REQUESTED;
        }

        if (commandLine.hasOption(CliOptions.HELP_OPTION)) {
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
        System.out.println("Version: " + version);
        LOG.debug("Version: {}", version);
    }

    private void printHelp() {
        String cmdLineSyntax = "java -jar elasticsearch-status-monitor.jar";
        String header = "This tool provides a quick overview of an Elasticsearch cluster and analyses the data for potential problems.";
        String footer = "Please report issues at: https://bitbucket.org/romanha/elasticsearch-status-monitor";

        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(cmdLineSyntax, header, cliOptions.getAvailableOptions(), footer, true);
    }
}
