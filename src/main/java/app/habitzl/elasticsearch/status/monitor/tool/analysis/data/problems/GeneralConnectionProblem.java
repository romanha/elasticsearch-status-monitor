/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;
import java.util.Objects;

public class GeneralConnectionProblem implements Problem {
    private static final long serialVersionUID = 1L;

    public static GeneralConnectionProblem create() {
        return new GeneralConnectionProblem("");
    }

    public static GeneralConnectionProblem create(final String additionalInformation) {
        return new GeneralConnectionProblem(additionalInformation);
    }

    private final String additionalInformation;

    private GeneralConnectionProblem(final String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public String getTitle() {
        return "General connection failure";
    }

    @Override
    public String getDescription() {
        return "The tool could not connect to the Elasticsearch cluster.";
    }

    @Override
    public String getSolution() {
        return "Check the host and port of the tool configuration. Check if the Elasticsearch cluster requires a secured connection (HTTPS).";
    }

    @Override
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    @Override
    public boolean equals(final Object o) {
        boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o == null || getClass() != o.getClass()) {
            isEqual = false;
        } else {
            GeneralConnectionProblem that = (GeneralConnectionProblem) o;
            isEqual = Objects.equals(additionalInformation, that.additionalInformation);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(additionalInformation);
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
