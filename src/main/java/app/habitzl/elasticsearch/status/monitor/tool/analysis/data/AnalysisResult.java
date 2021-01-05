package app.habitzl.elasticsearch.status.monitor.tool.analysis.data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import javax.annotation.concurrent.Immutable;

/**
 * Holds all problems and warnings found by an analyser.
 */
@Immutable
public class AnalysisResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Problem> problems;
    private final List<Warning> warnings;

    public static AnalysisResult empty() {
        return new AnalysisResult(List.of(), List.of());
    }

    public static AnalysisResult create(final List<Problem> problems, final List<Warning> warnings) {
        return new AnalysisResult(problems, warnings);
    }

    private AnalysisResult(final List<Problem> problems, final List<Warning> warnings) {
        this.problems = problems;
        this.warnings = warnings;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(final Object o) {
        boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o == null || getClass() != o.getClass()) {
            isEqual = false;
        } else {
            AnalysisResult that = (AnalysisResult) o;
            isEqual = Objects.equals(problems, that.problems)
                    && Objects.equals(warnings, that.warnings);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(problems, warnings);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AnalysisResult.class.getSimpleName() + "[", "]")
                .add("problems=" + problems)
                .add("warnings=" + warnings)
                .toString();
    }
}
