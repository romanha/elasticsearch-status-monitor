<#-- @ftlvariable name="report" type="app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport" -->
<#macro section>
    <div id="cluster-info" class="bordered-detail-section centered-detail-section">
        <h2>Cluster</h2>

        <table>
            <tbody>
            <tr>
                <td>Name:</td>
                <td>${report.clusterInfo.clusterName}</td>
            </tr>
            <tr>
                <td>Health status:</td>
                <td>
                    <span <#if report.clusterInfo.healthStatus == "RED">class="error"
                          <#elseif report.clusterInfo.healthStatus == "YELLOW">class="warn"
                          <#elseif report.clusterInfo.healthStatus == "GREEN">class="success"</#if>>
                            ${report.clusterInfo.healthStatus}
                    </span>
                </td>
            </tr>
            <tr>
                <td>Shard status:</td>
                <td>
                    ${report.clusterInfo.numberOfActiveShards} active /
                    <span <#if report.clusterInfo.numberOfUnassignedShards gt 0>class="warn"</#if>>
                        ${report.clusterInfo.numberOfUnassignedShards} unassigned
                    </span>
                </td>
            </tr>
            </tbody>
        </table>

        <h3>Nodes</h3>

        <table>
            <tbody>
            <tr>
                <td>Nodes:</td>
                <td>${report.clusterInfo.clusterStats.numberOfNodes}</td>
            </tr>
            <tr>
                <td>Master eligible nodes:</td>
                <td>${report.clusterInfo.clusterStats.numberOfMasterEligibleNodes}</td>
            </tr>
            <tr>
                <td>Data nodes:</td>
                <td>${report.clusterInfo.clusterStats.numberOfDataNodes}</td>
            </tr>
            </tbody>
        </table>

        <h3>Shards</h3>

        <table>
            <tbody>
            <tr>
                <td>Shards:</td>
                <td>${report.clusterInfo.clusterStats.numberOfShards}</td>
            </tr>
            <tr>
                <td>Primary shards:</td>
                <td>${report.clusterInfo.clusterStats.numberOfPrimaryShards}</td>
            </tr>
            <tr>
                <td>Replica shards:</td>
                <td>${report.clusterInfo.clusterStats.numberOfShards - report.clusterInfo.clusterStats.numberOfPrimaryShards}</td>
            </tr>
            </tbody>
        </table>

        <h3>Indices</h3>

        <table>
            <tbody>
            <tr>
                <td>Indices:</td>
                <td>${report.clusterInfo.clusterStats.numberOfIndices}</td>
            </tr>
            <tr>
                <td>Documents:</td>
                <td>${report.clusterInfo.clusterStats.numberOfDocuments}</td>
            </tr>
            </tbody>
        </table>
    </div>
</#macro>