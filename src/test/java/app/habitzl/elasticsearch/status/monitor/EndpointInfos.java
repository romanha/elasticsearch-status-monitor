package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;

/**
 * Utility class for creating random endpoint infos.
 */
public final class EndpointInfos {

    private static final int ONE_HUNDRED = 100;

    private EndpointInfos() {
        // instantiation protection
    }

    public static EndpointInfo random() {
        return new EndpointInfo(
                "Address " + Randoms.generateInteger(),
                Randoms.generateInteger(ONE_HUNDRED),
                Randoms.generateInteger(ONE_HUNDRED)
        );
    }
}
