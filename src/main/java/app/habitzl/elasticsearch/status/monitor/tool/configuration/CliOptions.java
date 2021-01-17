package app.habitzl.elasticsearch.status.monitor.tool.configuration;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Holds all available CLI options.
 */
public class CliOptions {

    protected static final String VERSION_OPTION = "v";
    protected static final String VERSION_OPTION_LONG = "version";
    protected static final String HELP_OPTION = "h";
    protected static final String HELP_OPTION_LONG = "help";
    protected static final String ADDRESS_OPTION = "a";
    protected static final String ADDRESS_OPTION_LONG = "address";
    protected static final String PORT_OPTION = "p";
    protected static final String PORT_OPTION_LONG = "port";
    protected static final String SECURITY_OPTION = "s";
    protected static final String SECURITY_OPTION_LONG = "security";
    protected static final String USER_OPTION = "u";
    protected static final String USER_OPTION_LONG = "username";
    protected static final String PASSWORD_OPTION = "x";
    protected static final String PASSWORD_OPTION_LONG = "password";

    private final Options availableOptions;

    protected CliOptions() {
        this.availableOptions = createOptions();
    }

    public Options getAvailableOptions() {
        return availableOptions;
    }

    private Options createOptions() {
        Options options = new Options();
        options.addOption(createVersionOption());
        options.addOption(createHelpOption());
        options.addOption(createHostOption());
        options.addOption(createPortOption());
        options.addOption(createSecurityOption());
        options.addOption(createUserOption());
        options.addOption(createPasswordOption());
        return options;
    }

    private Option createVersionOption() {
        return Option.builder(VERSION_OPTION)
                     .longOpt(VERSION_OPTION_LONG)
                     .hasArg(false)
                     .desc("Print the version number of this tool. By using this option no analysis is started.")
                     .build();
    }

    private Option createHelpOption() {
        return Option.builder(HELP_OPTION)
                     .longOpt(HELP_OPTION_LONG)
                     .hasArg(false)
                     .desc("Print a help message for using this tool. By using this option no analysis is started.")
                     .build();
    }

    private Option createHostOption() {
        return Option.builder(ADDRESS_OPTION)
                     .longOpt(ADDRESS_OPTION_LONG)
                     .hasArg(true)
                     .argName("IP address or host name")
                     .desc("The IP address or host name of the Elasticsearch endpoint.")
                     .build();
    }

    private Option createPortOption() {
        return Option.builder(PORT_OPTION)
                     .longOpt(PORT_OPTION_LONG)
                     .hasArg(true)
                     .argName("port")
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
