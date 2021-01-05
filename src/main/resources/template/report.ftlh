<#-- @ftlvariable name="report" type="app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Elasticsearch Status Report</title>
    <meta charset="utf-8">
    <style>
        body {
            font-family: Calibri, sans-serif;
            margin: 2em 4em;
            background-color: #f4f3f1;
        }

        .error {
            font-weight: bold;
            color: red;
        }

        .warn {
            font-weight: bold;
            color: orange;
        }

        .success {
            font-weight: bold;
            color: green;
        }

        .detail-section {
            width: max-content;
            margin-bottom: 2em;
        }

        .bordered-detail-section {
            width: max-content;
            padding: 1em;
            background: white;
            border: solid 1px darkgray;
            border-radius: 1em;
            box-shadow: 0.5em 0.5em 1em rgba(0, 0, 0, 0.1);

            margin-bottom: 2em;
        }

        /**
         * Reduces the top margin of the first heading in a bordered block.
         */
        .bordered-detail-section > h1:first-child, .bordered-detail-section > h2:first-child, .bordered-detail-section > h3:first-child {
            margin-top: 0.2em;
        }

        .multi-child-detail-section > div {
            width: max-content;
            margin-right: 6em;
            float: left;
            margin-bottom: 2em;
        }

        /*
         * This is used to properly clear the float after the section.
         * See: https://stackoverflow.com/questions/18717568/margin-not-working-with-float-elements
         */
        .multi-child-detail-section::after {
            clear: both;
            display: block;
            content: '';
            width: 100%;
            height: 0;
        }
    </style>
</head>
<body>
<h1>Elasticsearch Status Report</h1>

<div id="configuration" class="bordered-detail-section">
    <h2>Tool Configuration</h2>
    <table>
        <tbody>
        <tr>
            <td>Endpoint:</td>
            <td>${report.configuration.usingHttps?string("https://", "http://")}${report.configuration.host}:${report.configuration.port}</td>
        </tr>
        <tr>
            <td>Security:</td>
            <td>${report.configuration.usingHttps?string("enabled", "disabled")}</td>
        </tr>
        </tbody>
    </table>
</div>

<#if report.reportProgress == "FINISHED">
    <div id="cluster-info" class="bordered-detail-section">
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

    <div id="node-list-info" class="multi-child-detail-section">
        <#list report.nodeInfos as node>
            <div class="bordered-detail-section">
                <h2>Node: ${node.nodeName}<#if node.masterNode> &starf;</#if></h2>
                <h3>Endpoint Information</h3>
                <table>
                    <tbody>
                    <tr>
                        <td>IP address:</td>
                        <td>${node.endpointInfo.ipAddress}</td>
                    </tr>
                    <tr>
                        <td>RAM usage:</td>
                        <td>${node.endpointInfo.ramUsageInPercent}&percnt;</td>
                    </tr>
                    <tr>
                        <td>Heap usage:</td>
                        <td>${node.endpointInfo.heapUsageInPercent}&percnt;</td>
                    </tr>
                    </tbody>
                </table>

                <h3>Node Information</h3>
                <table>
                    <tbody>
                    <tr>
                        <td>Node ID:</td>
                        <td>${node.nodeId}</td>
                    </tr>
                    <tr>
                        <td>Process ID:</td>
                        <td>${node.processId}</td>
                    </tr>
                    <tr>
                        <td>Uptime:</td>
                        <td>${node.uptime}</td>
                    </tr>
                    </tbody>
                </table>

                <p>
                    <#if node.masterNode>&starf; This node is the current master node.<br/></#if>
                    <#if node.masterEligibleNode>&#10027; This node can become a master node.<br/></#if>
                    <#if node.dataNode>&#9776; This node holds search data.<br/></#if>
                </p>
            </div>
        </#list>
    </div>

    <div class="detail-section">
        <h2>More information is yet to come!</h2>
    </div>

<#elseif report.reportProgress=="ABORTED">
    <div class="error">
        <p>The status monitor was not able to create a full status report!</p>
    </div>
</#if>

<#if report.problems?size gt 0>
    <div id="problems" class="detail-section">
        <h2>Problems</h2>
        <#list report.problems as problem>
            <div class="bordered-detail-section">
                <h3>${problem.title}</h3>
                <p class="error">${problem.description}</p>
                <#if problem.additionalInformation?has_content>
                    <p>Additional information: ${problem.additionalInformation}</p>
                </#if>
                <p>&rarr; ${problem.solution}</p>
            </div>
        </#list>
    </div>
</#if>

<#if report.warnings?size gt 0>
    <div id="warnings" class="detail-section">
        <h2>Warnings</h2>
        <#list report.warnings as warning>
            <div class="bordered-detail-section">
                <h3>${warning.title}</h3>
                <p class="warn">${warning.description}</p>
                <#if warning.additionalInformation?has_content>
                    <p>Additional information: ${warning.additionalInformation}</p>
                </#if>
                <p>&rarr; ${warning.solution}</p>
            </div>
        </#list>
    </div>
</#if>

</body>
</html>