<#-- @ftlvariable name="report" type="app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport" -->
<#macro section>
    <div id="about" class="bordered-detail-section centered-detail-section">
        <h2>About</h2>
        <table>
            <tbody>
            <tr>
                <td>Version:</td>
                <td>${(report.toolVersion)!"Development Version"}</td>
            </tr>
            <tr>
                <td>Author:</td>
                <td>Roman Habitzl</td>
            </tr>
            <tr>
                <td>License:</td>
                <td>Apache License, Version 2.0</td>
            </tr>
            <tr>
                <td>Project page:</td>
                <td><a href="https://github.com/romanha/elasticsearch-status-monitor" target="_blank">Github</a></td>
            </tr>
            </tbody>
        </table>
    </div>
</#macro>