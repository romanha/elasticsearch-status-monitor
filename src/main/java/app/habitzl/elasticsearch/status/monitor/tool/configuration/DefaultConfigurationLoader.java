package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import app.habitzl.elasticsearch.status.monitor.ConfigurationLoader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class DefaultConfigurationLoader implements ConfigurationLoader {
    private static final Logger LOG = LogManager.getLogger(DefaultConfigurationLoader.class);

    static final String HOST_OPTION = "h";
    static final String HOST_OPTION_LONG = "host";
    static final String PORT_OPTION = "p";
    static final String PORT_OPTION_LONG = "port";
    static final String SECURITY_OPTION = "s";
    static final String SECURITY_OPTION_LONG = "security";
    static final String USER_OPTION = "u";
    static final String USER_OPTION_LONG = "username";
    static final String PASSWORD_OPTION = "x";
    static final String PASSWORD_OPTION_LONG = "password";

    private final StatusMonitorConfiguration configuration;

    @Inject
    public DefaultConfigurationLoader(final StatusMonitorConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void load(final String[] cliArguments) {
        parseCliOptions(cliArguments);
    }

    private void parseCliOptions(final String[] cliArguments) {
        Options allOptions = createOptions();
        DefaultParser cliParser = new DefaultParser();
        try {
            CommandLine commandLine = cliParser.parse(allOptions, cliArguments);
            parseConnectionOptions(commandLine);
            parseAuthenticationOptions(commandLine);
        } catch (final ParseException e) {
            LOG.error("Invalid options provided for starting the Elasticsearch Status Monitor. Using default configuration.", e);
        }
    }

    private void parseConnectionOptions(final CommandLine commandLine) {
        if (commandLine.hasOption(HOST_OPTION)) {
            LOG.info("Using host {} from CLI options.", commandLine.getOptionValue(HOST_OPTION));
            configuration.setHost(commandLine.getOptionValue(HOST_OPTION));
        }

        if (commandLine.hasOption(PORT_OPTION)) {
            LOG.info("Using configured port {} from CLI options.", commandLine.getOptionValue(PORT_OPTION));
            configuration.setPort(commandLine.getOptionValue(PORT_OPTION));
        }

        if (commandLine.hasOption(SECURITY_OPTION)) {
            boolean isSecurityDisabled = commandLine.getOptionValue(SECURITY_OPTION).equalsIgnoreCase("false");
            LOG.info("Security is {} by CLI options.", isSecurityDisabled ? "disabled" : "enabled");
            configuration.setUsingHttps(!isSecurityDisabled);
        }
    }

    private void parseAuthenticationOptions(final CommandLine commandLine) {
        if (commandLine.hasOption(USER_OPTION)) {
            LOG.info("Using configured user name {} from CLI options.", commandLine.getOptionValue(USER_OPTION));
            configuration.setUsername(commandLine.getOptionValue(USER_OPTION));
        }

        if (commandLine.hasOption(PASSWORD_OPTION)) {
            LOG.info("Using configured password from CLI options.");
            configuration.setPassword(commandLine.getOptionValue(PASSWORD_OPTION));
        }
    }

    private Options createOptions() {
        Options allOptions = new Options();
        allOptions.addOption(createHostOption());
        allOptions.addOption(createPortOption());
        allOptions.addOption(createSecurityOption());
        allOptions.addOption(createUserOption());
        allOptions.addOption(createPasswordOption());
        return allOptions;
    }

    private Option createHostOption() {
        return Option.builder(HOST_OPTION)
                     .longOpt(HOST_OPTION_LONG)
                     .hasArg(true)
                     .argName("IP address or host name")
                     .desc("The IP address or host name of the Elasticsearch endpoint.")
                     .build();
    }

    private Option createPortOption() {
        return Option.builder(PORT_OPTION)
                     .longOpt(PORT_OPTION_LONG)
                     .hasArg(true)
                     .argName("endpoint port")
                     .desc("The HTTP port of the Elasticsearch endpoint.")
                     .build();
    }

    private Option createSecurityOption() {
        return Option.builder(SECURITY_OPTION)
                     .longOpt(SECURITY_OPTION_LONG)
                     .hasArg(true)
                     .argName("true or false")
                     .desc("Enables or disables security for the tool. If disabled, the tool will not use HTTPS when connecting to Elasticsearch.")
                     .build();
    }

    private Option createUserOption() {
        return Option.builder(USER_OPTION)
                     .longOpt(USER_OPTION_LONG)
                     .hasArg(true)
                     .argName("user name")
                     .desc("The user name of the Elasticsearch user.")
                     .build();
    }

    private Option createPasswordOption() {
        return Option.builder(PASSWORD_OPTION)
                     .longOpt(PASSWORD_OPTION_LONG)
                     .hasArg(true)
                     .argName("password")
                     .desc("The password of the Elasticsearch user.")
                     .build();
    }
}
