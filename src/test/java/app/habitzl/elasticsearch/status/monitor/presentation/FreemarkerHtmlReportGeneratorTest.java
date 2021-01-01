package app.habitzl.elasticsearch.status.monitor.presentation;

import app.habitzl.elasticsearch.status.monitor.presentation.file.TemplateProcessor;
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
	void generate_dataModel_getTemplateFromConfiguration() throws Exception {
		// Given
		Object dataModel = mock(Object.class);
		prepareConfigurationTemplate();

		// When
		sut.generate(dataModel);

		// Then
		verify(configuration).getTemplate(FreemarkerHtmlReportGenerator.TEMPLATE_FILE_NAME);
	}

	@Test
	void generate_configurationReturnsTemplate_getTemplateFromConfiguration() throws Exception {
		// Given
		Object dataModel = mock(Object.class);
		Template template = prepareConfigurationTemplate();

		// When
		sut.generate(dataModel);

		// Then
		Map<String, Object> expectedModelMap = Map.of(FreemarkerHtmlReportGenerator.TEMPLATE_DATA_MODEL_REFERENCE, dataModel);
		verify(templateProcessor).processTemplate(template, expectedModelMap);
	}

	@Test
	void generate_configurationThrowsIOException_doNotRetry() throws Exception {
		// Given
		Object dataModel = mock(Object.class);
		givenConfigurationThrowsException();

		// When
		sut.generate(dataModel);

		// Then
		verify(configuration).getTemplate(anyString());
		verifyNoMoreInteractions(configuration);
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