package app.habitzl.elasticsearch.status.monitor.tool.analysis;

import app.habitzl.elasticsearch.status.monitor.AnalysisStartOption;
import app.habitzl.elasticsearch.status.monitor.ClusterInfos;
import app.habitzl.elasticsearch.status.monitor.ExitCode;
import app.habitzl.elasticsearch.status.monitor.NodeInfos;
import app.habitzl.elasticsearch.status.monitor.StatusMonitorConfigurations;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

class DefaultExitCodeMapperTest {

    private DefaultExitCodeMapper sut;

    @BeforeEach
    void setUp() {
        sut = new DefaultExitCodeMapper();
    }

    @Test
    void getExitCode_analysisWasNotRequested_returnExitCodeNoAnalysisRequired() {
        // When
        ExitCode result = sut.getExitCode(AnalysisStartOption.ANALYSIS_NOT_REQUESTED, null);

        // Then
        assertThat(result, equalTo(ExitCode.NO_ANALYSIS_REQUIRED));
    }

    @Test
    void getExitCode_analysisWasNotPossible_returnExitCodeMisconfiguration() {
        // When
        ExitCode result = sut.getExitCode(AnalysisStartOption.ANALYSIS_NOT_POSSIBLE, null);

        // Then
        assertThat(result, equalTo(ExitCode.MISCONFIGURATION));
    }

    /**
     * This case should never occur. Still check for null safety.
     */
    @Test
    void getExitCode_analysisPossibleButNullReport_returnExitCodeMisconfiguration() {
        // When
        ExitCode result = sut.getExitCode(AnalysisStartOption.ANALYSIS_POSSIBLE, null);

        // Then
        assertThat(result, equalTo(ExitCode.MISCONFIGURATION));
    }

    @Test
    void getExitCode_abortedAnalysisReport_returnExitCodeProblemsFound() {
        // Given
        List<Problem> problems = List.of();
        AnalysisReport report = AnalysisReport.aborted(StatusMonitorConfigurations.random(), problems);

        // When
        ExitCode result = sut.getExitCode(AnalysisStartOption.ANALYSIS_POSSIBLE, report);

        // Then
        assertThat(result, equalTo(ExitCode.ANALYSIS_ABORTED));
    }

    @Test
    void getExitCode_reportWithProblems_returnExitCodeProblemsFound() {
        // Given
        List<Problem> problems = List.of(mock(Problem.class));
        List<Warning> warnings = List.of();
        AnalysisReport report = createNewReport(problems, warnings);

        // When
        ExitCode result = sut.getExitCode(AnalysisStartOption.ANALYSIS_POSSIBLE, report);

        // Then
        assertThat(result, equalTo(ExitCode.PROBLEMS_FOUND));
    }

    @Test
    void getExitCode_reportWithProblemsAndWarnings_returnExitCodeProblemsFound() {
        // Given
        List<Problem> problems = List.of(mock(Problem.class));
        List<Warning> warnings = List.of(mock(Warning.class));
        AnalysisReport report = createNewReport(problems, warnings);

        // When
        ExitCode result = sut.getExitCode(AnalysisStartOption.ANALYSIS_POSSIBLE, report);

        // Then
        assertThat(result, equalTo(ExitCode.PROBLEMS_FOUND));
    }

    @Test
    void getExitCode_reportWithOnlyWarnings_returnExitCodeOnlyWarningsFound() {
        // Given
        List<Problem> problems = List.of();
        List<Warning> warnings = List.of(mock(Warning.class));
        AnalysisReport report = createNewReport(problems, warnings);

        // When
        ExitCode result = sut.getExitCode(AnalysisStartOption.ANALYSIS_POSSIBLE, report);

        // Then
        assertThat(result, equalTo(ExitCode.ONLY_WARNINGS_FOUND));
    }

    @Test
    void getExitCode_reportWithoutProblemsOrWarnings_returnExitCodeNoIssuesFound() {
        // Given
        List<Problem> problems = List.of();
        List<Warning> warnings = List.of();
        AnalysisReport report = createNewReport(problems, warnings);

        // When
        ExitCode result = sut.getExitCode(AnalysisStartOption.ANALYSIS_POSSIBLE, report);

        // Then
        assertThat(result, equalTo(ExitCode.NO_ISSUES_FOUND));
    }

    private AnalysisReport createNewReport(final List<Problem> problems, final List<Warning> warnings) {
        return AnalysisReport.finished(
                StatusMonitorConfigurations.random(),
                problems,
                warnings,
                ClusterInfos.random(),
                List.of(NodeInfos.random())
        );
    }
}