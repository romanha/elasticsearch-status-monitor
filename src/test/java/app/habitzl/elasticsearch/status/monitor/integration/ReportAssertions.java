package app.habitzl.elasticsearch.status.monitor.integration;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Warning;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

/**
 * Provides assertions for the generated HTML report file.
 */
public class ReportAssertions {
    private final Document htmlDocument;

    public static ReportAssertions create(final File reportFile) throws Exception {
        return new ReportAssertions(reportFile);
    }

    private ReportAssertions(final File reportFile) throws Exception {
        htmlDocument = Jsoup.parse(reportFile, StandardCharsets.UTF_8.name());
    }

    public void assertThatNoProblemsExist() {
        String xpath = "//div[@id='problems']";
        Elements elements = htmlDocument.selectXpath(xpath);
        assertThat(elements, empty());
    }

    public void assertThatNoWarningsExist() {
        String xpath = "//div[@id='warnings']";
        Elements elements = htmlDocument.selectXpath(xpath);
        assertThat(elements, empty());
    }

    /**
     * Validates whether the report lists the specified problem.
     */
    public void assertThatProblemExists(final Problem expectedProblem) {
        String xpath = String.format("//div[@id='problems']/div[*]/h3[contains(text(), '%s')]", expectedProblem.getTitle());
        Elements elements = htmlDocument.selectXpath(xpath);
        assertThat(elements, hasSize(1));
    }

    /**
     * Validates whether the report lists the specified warning.
     */
    public void assertThatWarningExists(final Warning expectedWarning) {
        String xpath = String.format("//div[@id='warnings']/div[*]/h3[contains(text(), '%s')]", expectedWarning.getTitle());
        Elements elements = htmlDocument.selectXpath(xpath);
        assertThat(elements, hasSize(1));
    }
}
