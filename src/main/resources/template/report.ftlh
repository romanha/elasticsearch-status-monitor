<#-- @ftlvariable name="report" type="app.habitzl.elasticsearch.status.monitor.presentation.model.StatusReport" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Elasticsearch Status Report</title>
    <meta charset="utf-8">
    <style>
        body {
            font-family: Calibri, sans-serif;
            margin: 2em 4em;
            background-color: #ececec;
        }

        .detail-section {
            width: max-content;
            margin-bottom: 2em;
        }

        .bordered-detail-section {
            width: max-content;
            padding: 1em;
            border: solid 2px black;
            border-radius: 2em;

            margin-bottom: 2em;
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
    </style>
</head>
<body>
<h1>Elasticsearch Status Report</h1>

<#if report.connectionStatus == "SUCCESS">
    <div id="cluster-info" class="bordered-detail-section">
        <h2>Cluster: ${report.clusterInfo.clusterName}</h2>
        <p>
            Health status:
            <span <#if report.clusterInfo.healthStatus == "RED">class="error"
                  <#elseif report.clusterInfo.healthStatus == "YELLOW">class="warn"
                  <#elseif report.clusterInfo.healthStatus == "GREEN">class="success"</#if>
            >${report.clusterInfo.healthStatus}</span>
        </p>
        <p>
            Shards:
            ${report.clusterInfo.numberOfActiveShards} active /
            <span <#if report.clusterInfo.numberOfUnassignedShards gt 0>class="warn"</#if>
            >${report.clusterInfo.numberOfUnassignedShards} unassigned</span>
        </p>
    </div>

    <div id="node-list-info" class="multi-child-detail-section">
        <#list report.nodeInfos as node>
            <div class="bordered-detail-section">
                <h2>Node: ${node.nodeName}</h2>
                <p>
                    Node ID: ${node.nodeId}<br/>
                    Process ID: ${node.processId}<br/>
                    Uptime: ${node.uptime}<br/>
                </p>

                <p>
                    <#if node.masterNode>This node is the current master node.<br/></#if>
                    <#if node.masterEligibleNode>This node can become a new master node.<br/></#if>
                    <#if node.dataNode>This node holds search data.<br/></#if>
                </p>

                <h3>Node Endpoint</h3>
                <p>
                    Address: ${node.endpointInfo.ipAddress}<br/>
                    RAM usage: ${node.endpointInfo.ramUsageInPercent}&percnt;<br/>
                    Heap usage: ${node.endpointInfo.heapUsageInPercent}&percnt;<br/>
                </p>
            </div>
        </#list>
    </div>

    <div class="detail-section">
        <span style="font-weight: bold">More information is yet to come!</span>
    </div>

<#else>
    <div class="error">
        <p>Could not connect to the Elasticsearch cluster!</p>
    </div>
</#if>

</body>
</html>