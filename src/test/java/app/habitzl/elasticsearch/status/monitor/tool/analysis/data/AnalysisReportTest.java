package app.habitzl.elasticsearch.status.monitor.tool.analysis.data;

import app.habitzl.elasticsearch.status.monitor.ClusterInfos;
import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

class AnalysisReportTest {

    @Test
    void createAbortedReport_noProblems_hasMonitoringResultProblemsFound() {
        // Given
        List<Problem> problems = List.of();

        // When
        AnalysisReport report = AnalysisReport.aborted(StatusMonitorConfigurations.random(), problems);

        // Then
        assertThat(report.getMonitoringResult(), equalTo(MonitoringResult.PROBLEMS_FOUND));
    }

    @Test
    void createReport_problems_hasMonitoringResultProblemsFound() {
        // Given
        List<Problem> problems = List.of(mock(Problem.class));
        List<Warning> warnings = List.of();

        // When
        AnalysisReport report = createNewReport(problems, warnings);

        // Then
        assertThat(report.getMonitoringResult(), equalTo(MonitoringResult.PROBLEMS_FOUND));
    }

    @Test
    void createReport_problemsAndWarnings_hasMonitoringResultProblemsFound() {
        // Given
        List<Problem> problems = List.of(mock(Problem.class));
        List<Warning> warnings = List.of(mock(Warning.class));

        // When
        AnalysisReport report = createNewReport(problems, warnings);

        // Then
        assertThat(report.getMonitoringResult(), equalTo(MonitoringResult.PROBLEMS_FOUND));
    }

    @Test
    void createReport_warnings_hasMonitoringResultOnlyWarningsFound() {
        // Given
        List<Problem> problems = List.of();
        List<Warning> warnings = List.of(mock(Warning.class));

        // When
        AnalysisReport report = createNewReport(problems, warnings);

        // Then
        assertThat(report.getMonitoringResult(), equalTo(MonitoringResult.ONLY_WARNINGS_FOUND));
    }

    @Test
    void createReport_noProblemsOrWarnings_hasMonitoringResultNoIssuesFound() {
        // Given
        List<Problem> problems = List.of();
        List<Warning> warnings = List.of();

        // When
        AnalysisReport report = createNewReport(problems, warnings);

        // Then
        assertThat(report.getMonitoringResult(), equalTo(MonitoringResult.NO_ISSUES_FOUND));
    }

    private AnalysisReport createNewReport(final List<Problem> problems, final List<Warning> warnings) {
        return AnalysisReport.create(
                StatusMonitorConfigurations.random(),
                problems,
                warnings,
                ClusterInfos.random(),
                List.of(NodeInfos.random())
        );
    }
}