/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.analysis.data.problems;

import app.habitzl.elasticsearch.status.monitor.tool.analysis.data.Problem;

public class SSLHandshakeFailure implements Problem {
    private static final long serialVersionUID = 1L;

    public static SSLHandshakeFailure create() {
        return new SSLHandshakeFailure();
    }

    private SSLHandshakeFailure() {
    }

    @Override
    public String getTitle() {
        return "SSL handshake failure";
    }

    @Override
    public String getDescription() {
        return "The tool and the Elasticsearch cluster could not negotiate the desired level of security.";
    }

    @Override
    public String getSolution() {
        return "Verify that the required certificates are available in the keystore of the Java runtime.";
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
