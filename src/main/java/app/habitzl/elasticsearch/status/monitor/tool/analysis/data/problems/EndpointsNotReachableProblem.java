/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;

import java.util.List;
import java.util.Objects;

public class EndpointsNotReachableProblem implements Problem {
    private static final long serialVersionUID = 1L;

    public static EndpointsNotReachableProblem create() {
        return new EndpointsNotReachableProblem(List.of());
    }

    public static EndpointsNotReachableProblem create(final List<String> endpoints) {
        return new EndpointsNotReachableProblem(endpoints);
    }

    private final List<String> endpoints;

    private EndpointsNotReachableProblem(final List<String> endpoints) {
        this.endpoints = Objects.isNull(endpoints) ? List.of() : List.copyOf(endpoints);
    }

    @Override
    public String getTitle() {
        return "Endpoints not reachable";
    }

    @Override
    public String getDescription() {
        return "The Elasticsearch cluster does not contain one or more of the configured endpoints.";
    }

    @Override
    public String getSolution() {
        return "Check if an Elasticsearch node is running on each of the configured endpoints.";
    }

    @Override
    public String getAdditionalInformation() {
        return "Following endpoints were not reachable: " + String.join(", ", endpoints);
    }

    public List<String> getEndpoints() {
        return endpoints;
    }

    @Override
    public boolean equals(final Object o) {
        boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o == null || getClass() != o.getClass()) {
            isEqual = false;
        } else {
            EndpointsNotReachableProblem that = (EndpointsNotReachableProblem) o;
            isEqual = Objects.equals(endpoints, that.endpoints);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoints);
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
