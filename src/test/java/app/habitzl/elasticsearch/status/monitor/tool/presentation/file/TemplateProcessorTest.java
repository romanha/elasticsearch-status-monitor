package app.habitzl.elasticsearch.status.monitor.tool.presentation.file;

import freemarker.template.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class TemplateProcessorTest {

    private TemplateProcessor sut;

    @BeforeEach
    void setUp() {
        sut = new TemplateProcessor(mock(File.class));
    }

    @Test
    void generate_nullReportFile_doNotProcessOnTemplate() {
        // Given
        Template template = mock(Template.class);
        sut = new TemplateProcessor(null);

        // When
        sut.processTemplate(template, mock(Object.class));

        // Then
        verifyNoInteractions(template);
    }

    @Test
    void generate_invalidReportFilePath_doNotProcessOnTemplate() {
        // Given
        Template template = mock(Template.class);
        File fileWithInvalidPath = mock(File.class);
        sut = new TemplateProcessor(fileWithInvalidPath);

        // When
        sut.processTemplate(template, mock(Object.class));

        // Then
        verifyNoInteractions(template);
    }
}