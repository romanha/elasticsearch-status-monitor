<#-- @ftlvariable name="report" type="app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport" -->
<#macro section>
    <div id="configuration" class="bordered-detail-section centered-detail-section">
        <h2>Tool Configuration</h2>
        <p>
            This report was generated at ${report.timestamp}.
        </p>
        <table>
            <tbody>
            <tr>
                <td>Endpoint:</td>
                <td>${report.configuration.usingHttps?string("https://", "http://")}${report.configuration.host}:${report.configuration.port}</td>
            </tr>
            <tr>
                <td>Fallback endpoints:</td>
                <td>
                    <#list report.configuration.fallbackEndpoints as endpoint>
                        ${report.configuration.usingHttps?string("https://", "http://")}${endpoint}<br/>
                    </#list>
                </td>
            </tr>
            <tr>
                <td>Security:</td>
                <td>${report.configuration.usingHttps?string("enabled", "disabled")}</td>
            </tr>
            <#if report.configuration.usingHttps>
                <tr>
                    <td>Username:</td>
                    <td>${report.configuration.username}</td>
                </tr>
            </#if>
            <tr>
                <td>Report files path:</td>
                <td>${report.configuration.reportFilesPath}</td>
            </tr>
            <tr>
                <td>Archive report:</td>
                <td>${report.configuration.isSkippingArchiveReport()?string("disabled", "enabled")}</td>
            </tr>
            </tbody>
        </table>
    </div>
</#macro>