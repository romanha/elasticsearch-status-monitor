package app.habitzl.elasticsearch.status.monitor.tool;

import app.habitzl.elasticsearch.status.monitor.AnalysisReports;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

class ReportBasedStatusMonitorTest {

    private ReportBasedStatusMonitor sut;
    private StatusAnalyser analyser;
    private ReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        analyser = mock(StatusAnalyser.class);
        reportGenerator = mock(ReportGenerator.class);
        sut = new ReportBasedStatusMonitor(analyser, reportGenerator);
    }

    @Test
    void createSnapshot_always_createReportFromAnalyser() {
        // When
        sut.createSnapshot();

        // Then
        verify(analyser).createReport();
    }

    @Test
    void createSnapshot_analyserReturnsReportModel_generatesReport() {
        // Given
        AnalysisReport report = prepareAnalyser();

        // When
        sut.createSnapshot();

        // Then
        verify(reportGenerator).generate(report);
    }

    @Test
    void createSnapshot_analyserReturnsReportModel_returnReport() {
        // Given
        AnalysisReport report = prepareAnalyser();

        // When
        AnalysisReport result = sut.createSnapshot();

        // Then
        assertThat(result, equalTo(report));
    }

    private AnalysisReport prepareAnalyser() {
        AnalysisReport report = AnalysisReports.random();
        when(analyser.createReport()).thenReturn(report);
        return report;
    }
}