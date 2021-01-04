package app.habitzl.elasticsearch.status.monitor.tool.analysis.data;

import java.io.Serializable;

/**
 * Defines a problem found by the automatic analysis of this tool.
 */
public interface Problem extends Serializable {

    String getTitle();

    String getDescription();

    String getSolution();

    default String getAdditionalInformation() {
        return "";
    }
}
