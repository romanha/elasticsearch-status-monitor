/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.client.data.version;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

public class ElasticsearchVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ElasticsearchMajorVersion majorVersion;
    private final String fullVersion;

    public static ElasticsearchVersion defaultVersion() {
        return new ElasticsearchVersion("unknown");
    }

    public ElasticsearchVersion(final String fullVersion) {
        this.majorVersion = ElasticsearchMajorVersion.fromVersionString(fullVersion);
        this.fullVersion = fullVersion;
    }

    public ElasticsearchMajorVersion getMajorVersion() {
        return majorVersion;
    }

    public String getFullVersion() {
        return fullVersion;
    }

    @Override
    public boolean equals(final Object o) {
        boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o == null || getClass() != o.getClass()) {
            isEqual = false;
        } else {
            ElasticsearchVersion that = (ElasticsearchVersion) o;
            isEqual = Objects.equals(majorVersion, that.majorVersion)
                    && Objects.equals(fullVersion, that.fullVersion);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorVersion, fullVersion);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ElasticsearchVersion.class.getSimpleName() + "[", "]")
                .add("majorVersion=" + majorVersion)
                .add("fullVersion='" + fullVersion + "'")
                .toString();
    }
}
