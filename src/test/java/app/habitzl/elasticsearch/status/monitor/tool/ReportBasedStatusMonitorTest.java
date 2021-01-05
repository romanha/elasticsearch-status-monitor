package app.habitzl.elasticsearch.status.monitor.tool;

import app.habitzl.elasticsearch.status.monitor.AnalysisReports;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void createSnapshot_analyserReturnsReportModel_generatesReport() {
        // Given
        AnalysisReport report = prepareAnalyser();

        // When
        sut.createSnapshot();

        // Then
        verify(analyser).createReport();
        verify(reportGenerator).generate(report);
    }

    private AnalysisReport prepareAnalyser() {
        AnalysisReport report = AnalysisReports.random();
        when(analyser.createReport()).thenReturn(report);
        return report;
    }
}