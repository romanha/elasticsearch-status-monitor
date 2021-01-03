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

	static final String IP_OPTION = "ip";
	static final String PORT_OPTION = "p";
	static final String UNSECURE_OPTION = "unsecure";

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
			if (commandLine.hasOption(IP_OPTION)) {
				LOG.info("Using IP address {} from CLI options.", commandLine.getOptionValue(IP_OPTION));
				configuration.setIpAddress(commandLine.getOptionValue(IP_OPTION));
			}
			if (commandLine.hasOption(PORT_OPTION)) {
				LOG.info("Using configured port {} from CLI options.", commandLine.getOptionValue(PORT_OPTION));
				configuration.setPort(commandLine.getOptionValue(PORT_OPTION));
			}
			if (commandLine.hasOption(UNSECURE_OPTION)) {
				LOG.info("Security is disabled by CLI options.");
				configuration.setUsingHttps(false);
			}
		} catch (final ParseException e) {
			LOG.error("Invalid options provided for starting the Elasticsearch Status Monitor. Using default configuration.", e);
		}
	}

	private Options createOptions() {
		Options allOptions = new Options();
		allOptions.addOption(createIpOption());
		allOptions.addOption(createPortOption());
		allOptions.addOption(createUnsecureOption());
		return allOptions;
	}

	private Option createIpOption() {
		return Option.builder(IP_OPTION)
					 .longOpt("ipAddress")
					 .hasArg(true)
					 .argName("endpoint address")
					 .desc("The IP address of the Elasticsearch endpoint.")
					 .build();
	}

	private Option createPortOption() {
		return Option.builder(PORT_OPTION)
					 .longOpt("port")
					 .hasArg(true)
					 .argName("endpoint port")
					 .desc("The HTTP port of the Elasticsearch endpoint.")
					 .build();
	}

	private Option createUnsecureOption() {
		return Option.builder(UNSECURE_OPTION)
					 .hasArg(false)
					 .desc("Disables security for the tool. The tool will not use HTTPS when connecting to Elasticsearch.")
					 .build();
	}
}
