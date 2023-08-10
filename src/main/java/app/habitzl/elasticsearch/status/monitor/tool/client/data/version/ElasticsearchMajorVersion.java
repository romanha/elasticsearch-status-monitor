/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.client.data.version;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents all supported Elasticsearch major versions.
 */
public enum ElasticsearchMajorVersion {

    UNKNOWN,

    SIX,

    SEVEN,

    EIGHT;

    private static final Pattern REGEX = Pattern.compile("^(\\d*)");

    public static ElasticsearchMajorVersion fromVersionString(final String version) {
        ElasticsearchMajorVersion result = UNKNOWN;

        if (Objects.nonNull(version)) {
            Matcher matcher = REGEX.matcher(version);
            if (matcher.find()) {
                String majorVersion = matcher.group(1);
                result = fromMajorVersionString(majorVersion);
            }
        }

        return result;
    }

    private static ElasticsearchMajorVersion fromMajorVersionString(final String majorVersion) {
        ElasticsearchMajorVersion result;
        switch (majorVersion) {
            case "6":
                result = SIX;
                break;
            case "7":
                result = SEVEN;
                break;
            case "8":
                result = EIGHT;
                break;
            default:
                result = UNKNOWN;
        }

        return result;
    }
}
