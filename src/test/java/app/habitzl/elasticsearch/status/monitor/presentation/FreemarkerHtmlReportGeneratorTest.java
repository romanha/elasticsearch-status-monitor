package app.habitzl.elasticsearch.status.monitor.presentation;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.mockito.Mockito.*;

// TODO: provide file writer via dependency injection to be mocked within test
class FreemarkerHtmlReportGeneratorTest {

	private FreemarkerHtmlReportGenerator sut;
	private Configuration configuration;
	private File reportFile;

	@BeforeEach
	void setUp() {
		configuration = mock(Configuration.class);
		reportFile = mock(File.class);
		sut = new FreemarkerHtmlReportGenerator(configuration, reportFile);
	}

	@Test
	@Disabled("enable when report file writer is provided as via ctor")
	void generate_dataModel_getTemplateFromConfiguration() throws IOException {
		// Given
		Object dataModel = mock(Object.class);

		// When
		sut.generate(dataModel);

		// Then
		verify(configuration).getTemplate(FreemarkerHtmlReportGenerator.TEMPLATE_FILE_NAME);
	}

	@Test
	@Disabled("enable when report file writer is provided as via ctor")
	void generate_configurationReturnsTemplate_getTemplateFromConfiguration() throws IOException, TemplateException {
		// Given
		Object dataModel = mock(Object.class);
		Template template = mock(Template.class);
		when(configuration.getTemplate(FreemarkerHtmlReportGenerator.TEMPLATE_FILE_NAME))
				.thenReturn(template);

		// When
		sut.generate(dataModel);

		// Then
		FileWriter expectedFileWriter = new FileWriter(reportFile);
		verify(template).process(dataModel, expectedFileWriter);
	}
}