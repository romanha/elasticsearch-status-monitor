package app.habitzl.elasticsearch.status.monitor.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAnd;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class HostnameResolverTest {

    private HostnameResolver sut;

    @BeforeEach
    void setUp() {
        sut = new HostnameResolver();
    }

    @Test
    void resolve_hostNameOfLocalhost_returnLocalIpAddress() throws Exception {
        // Given
        InetAddress localHost = InetAddress.getLocalHost();
        String hostHostName = localHost.getHostName();

        // When
        Optional<InetAddress> result = sut.resolve(hostHostName);

        // Then
        assertThat(result, isPresentAnd(equalTo(localHost)));
    }
}