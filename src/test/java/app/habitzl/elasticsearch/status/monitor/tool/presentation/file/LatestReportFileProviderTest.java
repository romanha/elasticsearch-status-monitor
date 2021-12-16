package app.habitzl.elasticsearch.status.monitor.tool.presentation.file;

import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import app.habitzl.elasticsearch.status.monitor.tool.configuration.StatusMonitorConfiguration;
import app.habitzl.elasticsearch.status.monitor.util.FileCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;

class LatestReportFileProviderTest {

    private LatestReportFileProvider sut;
    private FileCreator fileCreator;
    private StatusMonitorConfiguration configuration;

    @BeforeEach
    void setUp() {
        fileCreator = mock(FileCreator.class);
        configuration = StatusMonitorConfigurations.random();
        sut = new LatestReportFileProvider(fileCreator, configuration);
    }

    @Test
    void get_reportFileNotYetExisting_createsReportFile() throws Exception {
        // Given
        // report file not existing

        // When
        sut.get();

        // Then
        Path expectedPath = Paths.get(configuration.getReportFilesPath(), getExpectedDirectoryName());
        verify(fileCreator).create(expectedPath);
    }

    @Test
    void get_reportFileExists_doNotCreateReportFile() throws Exception {
        // Given
        givenReportFileAlreadyExists();

        // When
        sut.get();

        // Then
        verifyNoMoreInteractions(fileCreator);
    }

    @Test
    void get_reportFileNotExistingYet_returnNewReportFile() throws Exception {
        // Given
        prepareFileCreator();

        // When
        File result = sut.get();

        // Then
        File expectedResult = Paths.get(configuration.getReportFilesPath(), getExpectedDirectoryName(), ArchiveReportFileProvider.REPORT_FILE_NAME)
                .toFile();
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    void get_reportFileExists_returnSameReportFile() throws Exception {
        // Given
        File existingFile = givenReportFileAlreadyExists();

        // When
        File result = sut.get();

        // Then
        assertThat(result, equalTo(existingFile));
    }

    @Test
    void get_fileCreatorThrowsException_returnNull() throws Exception {
        // Given
        when(fileCreator.create(any(Path.class))).thenThrow(IOException.class);

        // When
        File result = sut.get();

        // Then
        assertThat(result, nullValue());
    }

    private void prepareFileCreator() throws IOException {
        Path path = Paths.get(configuration.getReportFilesPath(), getExpectedDirectoryName());
        when(fileCreator.create(path)).thenReturn(path);
    }

    private String getExpectedDirectoryName() {
        return LatestReportFileProvider.DIRECTORY_NAME;
    }

    private File givenReportFileAlreadyExists() throws IOException {
        prepareFileCreator();
        File file = sut.get();
        verify(fileCreator).create(any(Path.class));
        return file;
    }
}