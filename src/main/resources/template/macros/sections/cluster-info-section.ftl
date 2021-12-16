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
                <td>Shards:</td>
                <td>
                    ${report.clusterInfo.numberOfActiveShards} active /
                    <span <#if report.clusterInfo.numberOfUnassignedShards gt 0>class="warn"</#if>>
                        ${report.clusterInfo.numberOfUnassignedShards} unassigned
                    </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</#macro>