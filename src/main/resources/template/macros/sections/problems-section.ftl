<#-- @ftlvariable name="report" type="app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport" -->
<#macro section>
    <div id="problems" class="detail-section">
        <h2>Problems</h2>
        <#list report.problems as problem>
            <div class="bordered-detail-section">
                <h3>${problem.title}</h3>
                <p class="error">${problem.description}</p>
                <#if problem.additionalInformation?has_content>
                    <p>
                        <span class="bold">Additional information:</span>
                        <br/>
                        <span class="keep-new-lines">${problem.additionalInformation}</span>
                    </p>
                </#if>
                <p>
                    <span class="bold">Solution:</span>
                    <br/>
                    <span class="keep-new-lines">${problem.solution}</span>
                </p>
            </div>
        </#list>
    </div>
</#macro>