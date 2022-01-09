<#-- @ftlvariable name="report" type="app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport" -->
<#macro section>
    <div id="warnings" class="detail-section">
        <h2>Warnings (${report.warnings?size})</h2>
        <#list report.warnings as warning>
            <div class="bordered-detail-section">
                <h3>${warning.title}</h3>
                <p class="warn">${warning.description}</p>
                <#if warning.additionalInformation?has_content>
                    <p>
                        <span class="bold">Additional information:</span>
                        <br/>
                        <span class="keep-new-lines">${warning.additionalInformation}</span>
                    </p>
                </#if>
                <p>
                    <span class="bold">Solution:</span>
                    <br/>
                    <span class="keep-new-lines">${warning.solution}</span>
                </p>
            </div>
        </#list>
    </div>
</#macro>