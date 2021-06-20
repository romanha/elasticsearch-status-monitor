package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.ReportBasedStatusMonitor;
import app.habitzl.elasticsearch.status.monitor.tool.ReportGenerator;
import app.habitzl.elasticsearch.status.monitor.tool.StatusAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.DefaultExitCodeMapper;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.DefaultStatusAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.ElasticsearchClient;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.AnalyserProvider;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.ClusterAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.EndpointAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.analyser.ShardAnalyser;
import app.habitzl.elasticsearch.status.monitor.tool.client.DefaultElasticsearchClient;
import app.habitzl.elasticsearch.status.monitor.tool.client.InfoMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.ResponseMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.connection.ElasticsearchRestClientFactory;
import app.habitzl.elasticsearch.status.monitor.tool.client.connection.FallbackElasticsearchRestClientFactory;
import app.habitzl.elasticsearch.status.monitor.tool.client.connection.FallbackRestClientFactory;
import app.habitzl.elasticsearch.status.monitor.tool.client.connection.RestClientFactory;
import app.habitzl.elasticsearch.status.monitor.tool.client.connection.RestClientProvider;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.ClusterAllocationMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.ClusterInfoMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.ClusterSettingsMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.DefaultClusterAllocationMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.DefaultClusterInfoMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.DefaultClusterSettingsMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.DefaultInfoMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.DefaultNodeInfoMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.DefaultTimeParser;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.JsonContentResponseMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.NodeInfoMapper;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.TimeParser;
import app.habitzl.elasticsearch.status.monitor.tool.client.mapper.utils.JsonParser;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.CliOptions;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.DefaultConfigurationLoader;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import app.habitzl.elasticsearch.status.monitor.tool.presentation.FreemarkerHtmlReportGenerator;
import app.habitzl.elasticsearch.status.monitor.tool.presentation.configuration.FreemarkerConfigurationProvider;
import app.habitzl.elasticsearch.status.monitor.tool.presentation.file.ReportFile;
import app.habitzl.elasticsearch.status.monitor.tool.presentation.file.ReportFileProvider;
import app.habitzl.elasticsearch.status.monitor.tool.presentation.file.TemplateProcessor;
import app.habitzl.elasticsearch.status.monitor.util.ClockProvider;
import app.habitzl.elasticsearch.status.monitor.util.FileCreator;
import app.habitzl.elasticsearch.status.monitor.util.TimeFormatter;
import app.habitzl.elasticsearch.status.monitor.util.format.DayBasedTimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import freemarker.template.Configuration;
import org.elasticsearch.client.RestClient;

import java.io.File;
import java.time.Clock;

/**
 * A Google Guice module for defining bindings for the projects dependency injection.
 */
class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StatusMonitor.class).to(ReportBasedStatusMonitor.class).in(Singleton.class);
        bind(ExitCodeMapper.class).to(DefaultExitCodeMapper.class).in(Singleton.class);

        // Configuration
        bind(StatusMonitorConfiguration.class).in(Singleton.class);
        bind(ConfigurationLoader.class).to(DefaultConfigurationLoader.class).in(Singleton.class);
        bind(CliOptions.class).in(Singleton.class);

        // Elasticsearch client
        bind(ElasticsearchClient.class).to(DefaultElasticsearchClient.class).in(Singleton.class);
        bind(RestClient.class).toProvider(RestClientProvider.class).in(Singleton.class);
        bind(RestClientFactory.class).to(ElasticsearchRestClientFactory.class).in(Singleton.class);
        bind(FallbackRestClientFactory.class).to(FallbackElasticsearchRestClientFactory.class).in(Singleton.class);

        // Mapper and parser
        bind(JsonParser.class).in(Singleton.class);
        bind(ObjectMapper.class).in(Singleton.class);
        bind(ResponseMapper.class).to(JsonContentResponseMapper.class).in(Singleton.class);
        bind(TimeParser.class).to(DefaultTimeParser.class).in(Singleton.class);
        bind(InfoMapper.class).to(DefaultInfoMapper.class).in(Singleton.class);
        bind(ClusterSettingsMapper.class).to(DefaultClusterSettingsMapper.class).in(Singleton.class);
        bind(ClusterInfoMapper.class).to(DefaultClusterInfoMapper.class).in(Singleton.class);
        bind(NodeInfoMapper.class).to(DefaultNodeInfoMapper.class).in(Singleton.class);
        bind(ClusterAllocationMapper.class).to(DefaultClusterAllocationMapper.class).in(Singleton.class);

        // Analyser
        bind(StatusAnalyser.class).to(DefaultStatusAnalyser.class).in(Singleton.class);
        bind(AnalyserProvider.class).in(Singleton.class);
        bind(EndpointAnalyser.class).in(Singleton.class);
        bind(ClusterAnalyser.class).in(Singleton.class);
        bind(ShardAnalyser.class).in(Singleton.class);

        // Presentation
        bind(ReportGenerator.class).to(FreemarkerHtmlReportGenerator.class).in(Singleton.class);
        bind(File.class).annotatedWith(ReportFile.class).toProvider(ReportFileProvider.class);
        bind(TemplateProcessor.class).in(Singleton.class);
        bind(Configuration.class).toProvider(FreemarkerConfigurationProvider.class).in(Singleton.class);
        bind(TimeFormatter.class).to(DayBasedTimeFormatter.class).in(Singleton.class);

        // Utilities
        bind(Clock.class).toProvider(ClockProvider.class).in(Singleton.class);
        bind(FileCreator.class).in(Singleton.class);
    }
}
