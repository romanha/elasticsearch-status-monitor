package app.habitzl.elasticsearch.status.monitor.tool.presentation;

import app.habitzl.elasticsearch.status.monitor.AnalysisReports;
import app.habitzl.elasticsearch.status.monitor.tool.presentation.file.TemplateProcessor;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.*;

class FreemarkerHtmlReportGeneratorTest {

	private FreemarkerHtmlReportGenerator sut;
	private Configuration configuration;
	private TemplateProcessor templateProcessor;

	@BeforeEach
	void setUp() {
		configuration = mock(Configuration.class);
		templateProcessor = mock(TemplateProcessor.class);
		sut = new FreemarkerHtmlReportGenerator(configuration, templateProcessor);
	}

	@Test
	void generate_report_getTemplateFromConfiguration() throws Exception {
		// Given
		AnalysisReport report = createAnalysisReport();
		prepareConfigurationTemplate();

		// When
		sut.generate(report);

		// Then
		verify(configuration).getTemplate(FreemarkerHtmlReportGenerator.TEMPLATE_FILE_NAME);
	}

	@Test
	void generate_configurationReturnsTemplate_getTemplateFromConfiguration() throws Exception {
		// Given
		AnalysisReport report = createAnalysisReport();
		Template template = prepareConfigurationTemplate();

		// When
		sut.generate(report);

		// Then
		Map<String, Object> expectedModelMap = Map.of(FreemarkerHtmlReportGenerator.TEMPLATE_DATA_MODEL_REFERENCE, report);
		verify(templateProcessor).processTemplate(template, expectedModelMap);
	}

	@Test
	void generate_configurationThrowsIOException_doNotRetry() throws Exception {
		// Given
		AnalysisReport report = createAnalysisReport();
		givenConfigurationThrowsException();

		// When
		sut.generate(report);

		// Then
		verify(configuration).getTemplate(anyString());
		verifyNoMoreInteractions(configuration);
	}

	private AnalysisReport createAnalysisReport() {
		return AnalysisReports.random();
	}

	private Template prepareConfigurationTemplate() throws IOException {
		Template template = mock(Template.class);
		when(configuration.getTemplate(FreemarkerHtmlReportGenerator.TEMPLATE_FILE_NAME))
				.thenReturn(template);
		return template;
	}

	private void givenConfigurationThrowsException() throws IOException {
		when(configuration.getTemplate(FreemarkerHtmlReportGenerator.TEMPLATE_FILE_NAME))
				.thenThrow(IOException.class);
	}
}