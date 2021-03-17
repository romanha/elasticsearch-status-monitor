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
        return random(Randoms.generateInteger(ONE_HUNDRED));
    }

    public static EndpointInfo random(final int ramUsage) {
        return new EndpointInfo(
                Randoms.generateString("ip-address-"),
                Randoms.generateString("operating-system-"),
                Randoms.generatePositiveInteger(),
                Randoms.generateFloat(),
                ramUsage,
                Randoms.generatePositiveInteger()
        );
    }
}
