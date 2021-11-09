package app.habitzl.elasticsearch.status.monitor.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * Utility class for resolving hostnames and returning the according IP address.
 */
public class HostnameResolver {
    private static final Logger LOG = LogManager.getLogger(HostnameResolver.class);

    /**
     * Resolves the hostname and returns the IP address.
     *
     * @return the resolved IP address or empty, if the hostname cannot be resolved
     */
    public Optional<InetAddress> resolve(final String hostname) {
        InetAddress resolvedAddress = null;
        try {
            resolvedAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            LOG.warn("Could not resolve host '{}'.", hostname);
        }

        return Optional.ofNullable(resolvedAddress);
    }
}
