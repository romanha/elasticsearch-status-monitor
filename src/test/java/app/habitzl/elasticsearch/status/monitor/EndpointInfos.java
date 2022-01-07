package app.habitzl.elasticsearch.status.monitor;

import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.EndpointInfo;

/**
 * Utility class for creating random endpoint infos.
 */
public final class EndpointInfos {

    private EndpointInfos() {
        // instantiation protection
    }

    public static EndpointInfo random() {
        return random(Randoms.generatePercentage());
    }

    public static EndpointInfo random(final String httpPublishAddress) {
        return random(httpPublishAddress, Randoms.generatePercentage());
    }

    public static EndpointInfo random(final int ramUsage) {
        return random(Hosts.randomAddress(), ramUsage);
    }

    public static EndpointInfo random(final String httpPublishAddress, final int ramUsage) {
        return new EndpointInfo(
                Hosts.randomHostName(),
                httpPublishAddress,
                Randoms.generateString("operating-system-"),
                Randoms.generatePositiveInteger(),
                Randoms.generatePercentage(),
                Randoms.generateFloat(),
                ramUsage,
                Randoms.generatePositiveInteger()
        );
    }
}
