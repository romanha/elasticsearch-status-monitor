/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;

public class UnauthorizedConnectionProblem implements Problem {
    private static final long serialVersionUID = 1L;

    public static UnauthorizedConnectionProblem create() {
        return new UnauthorizedConnectionProblem();
    }

    private UnauthorizedConnectionProblem() {
    }

    @Override
    public String getTitle() {
        return "Unauthorized connection";
    }

    @Override
    public String getDescription() {
        return "The tool is not authorized to connect to the Elasticsearch cluster.";
    }

    @Override
    public String getSolution() {
        return "Check username and password for connecting to the cluster.";
    }

    @Override
    public boolean equals(final Object o) {
        boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else {
            isEqual = o != null && getClass() == o.getClass();
        }

        return isEqual;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
