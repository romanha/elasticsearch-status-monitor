/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster;

import app.habitzl.elasticsearch.status.monitor.ClusterSettingsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ClusterSettingsBuilderTest {

    private ClusterSettingsBuilder sut;

    @BeforeEach
    void init() {
        sut = ClusterSettingsBuilder.create();
    }

    @Test
    void build_noPropertiesSet_returnDefault() {
        // Given
        // no properties set

        // When
        ClusterSettings result = sut.build();

        // Then
        assertThat(result, equalTo(ClusterSettings.createDefault()));
    }

    @Test
    void build_propertiesSet_returnExpectedSettings() {
        // Given
        ClusterSettings expected = ClusterSettingsUtils.random();
        sut.withMinimumOfRequiredMasterNodesForElection(expected.getMinimumOfRequiredMasterNodesForElection());

        // When
        ClusterSettings result = sut.build();

        // Then
        assertThat(result, equalTo(expected));
        assertThat(result.getMinimumOfRequiredMasterNodesForElection(), equalTo(expected.getMinimumOfRequiredMasterNodesForElection()));
    }
}