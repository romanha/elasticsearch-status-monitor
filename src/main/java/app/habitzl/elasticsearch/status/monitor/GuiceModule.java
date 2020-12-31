package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.presentation.DefaultStatusAggregator;
import app.habitzl.elasticsearch.status.monitor.presentation.FreemarkerHtmlReportGenerator;
import app.habitzl.elasticsearch.status.monitor.presentation.StatusAggregator;
import app.habitzl.elasticsearch.status.monitor.presentation.TimeFormatter;
import app.habitzl.elasticsearch.status.monitor.presentation.configuration.FreemarkerConfigurationProvider;
import app.habitzl.elasticsearch.status.monitor.presentation.file.ReportFile;
import app.habitzl.elasticsearch.status.monitor.presentation.file.ReportFileProvider;
import app.habitzl.elasticsearch.status.monitor.presentation.format.DayBasedTimeFormatter;
import app.habitzl.elasticsearch.status.monitor.tool.ElasticsearchStatusMonitor;
import app.habitzl.elasticsearch.status.monitor.tool.InfoParser;
import app.habitzl.elasticsearch.status.monitor.tool.ResponseMapper;
import app.habitzl.elasticsearch.status.monitor.tool.connection.ElasticsearchRestClientFactory;
import app.habitzl.elasticsearch.status.monitor.tool.connection.RestClientFactory;
import app.habitzl.elasticsearch.status.monitor.tool.connection.RestClientProvider;
import app.habitzl.elasticsearch.status.monitor.tool.mapper.DefaultInfoParser;
import app.habitzl.elasticsearch.status.monitor.tool.mapper.DefaultNodeInfoParser;
import app.habitzl.elasticsearch.status.monitor.tool.mapper.DefaultTimeParser;
import app.habitzl.elasticsearch.status.monitor.tool.mapper.JsonContentResponseMapper;
import app.habitzl.elasticsearch.status.monitor.tool.mapper.NodeInfoParser;
import app.habitzl.elasticsearch.status.monitor.tool.mapper.TimeParser;
import app.habitzl.elasticsearch.status.monitor.util.ClockProvider;
import app.habitzl.elasticsearch.status.monitor.util.FileCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import freemarker.template.Configuration;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.File;
import java.time.Clock;

/**
 * A Google Guice module for defining bindings for the projects dependency injection.
 */
class GuiceModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(StatusMonitor.class).to(ElasticsearchStatusMonitor.class).in(Singleton.class);

		// Elasticsearch REST client
		bind(RestHighLevelClient.class).toProvider(RestClientProvider.class).in(Singleton.class);
		bind(RestClientFactory.class).to(ElasticsearchRestClientFactory.class).in(Singleton.class);

		// Mapper and parser
		bind(ObjectMapper.class).toInstance(new ObjectMapper());
		bind(ResponseMapper.class).to(JsonContentResponseMapper.class).in(Singleton.class);
		bind(TimeParser.class).to(DefaultTimeParser.class).in(Singleton.class);
		bind(InfoParser.class).to(DefaultInfoParser.class).in(Singleton.class);
		bind(NodeInfoParser.class).to(DefaultNodeInfoParser.class).in(Singleton.class);

		// Presentation
		bind(StatusAggregator.class).to(DefaultStatusAggregator.class).in(Singleton.class);
		bind(ReportGenerator.class).to(FreemarkerHtmlReportGenerator.class).in(Singleton.class);
		bind(File.class).annotatedWith(ReportFile.class).toProvider(ReportFileProvider.class);
		bind(Configuration.class).toProvider(FreemarkerConfigurationProvider.class);
		bind(TimeFormatter.class).to(DayBasedTimeFormatter.class).in(Singleton.class);

		// Utilities
		bind(Clock.class).toProvider(ClockProvider.class);
		bind(FileCreator.class).in(Singleton.class);
	}
}
