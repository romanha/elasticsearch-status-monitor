package app.habitzl.elasticsearch.status.monitor.presentation.configuration;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import javax.inject.Provider;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A provider responsible for returning a single instance of an Apache FreeMarker configuration.
 * FreeMarker {@link Configuration} should not be recreated, because doing so loses the template cache.
 * Therefore the configuration should be injected as application wide Singleton.
 */
public class FreemarkerConfigurationProvider implements Provider<Configuration> {

	private static final String DEFAULT_TEMPLATE_DIRECTORY = "/template";

	private Configuration configuration;

	@Override
	public Configuration get() {
		return Objects.nonNull(configuration) ? configuration : createConfiguration();
	}

	private Configuration createConfiguration() {
		Configuration config = new Configuration(Configuration.VERSION_2_3_30);
		config.setClassForTemplateLoading(this.getClass(), DEFAULT_TEMPLATE_DIRECTORY);
		config.setDefaultEncoding(StandardCharsets.UTF_8.name());
		config.setLogTemplateExceptions(false);
		config.setWrapUncheckedExceptions(true);
		config.setFallbackOnNullLoopVariable(false);

		// use TemplateExceptionHandler.HTML_DEBUG_HANDLER during development and RETHROW_HANDLER for production
		config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

		this.configuration = config;
		return config;
	}
}
