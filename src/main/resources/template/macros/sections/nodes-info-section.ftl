<#-- @ftlvariable name="report" type="app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport" -->
<#macro section>
    <div id="node-list-info" class="node-list-grid-container">
        <#list report.nodeInfos as node>
            <div class="node-list-grid-item">
                <div class="bordered-detail-section centered-detail-section">
                    <h2>Node: ${node.nodeName}<#if node.masterNode> &#11088;</#if></h2>

                    <div>
                        <h3>Endpoint Information</h3>

                        <table>
                            <tbody>
                            <tr>
                                <td>IP address:</td>
                                <td>${node.endpointInfo.ipAddress}</td>
                            </tr>
                            <tr>
                                <td>HTTP publish address:</td>
                                <td>${node.endpointInfo.httpPublishAddress}</td>
                            </tr>
                            <tr>
                                <td>Operating system:</td>
                                <td>${node.endpointInfo.operatingSystemName}</td>
                            </tr>
                            <tr>
                                <td>RAM usage:</td>
                                <td>
                                    <#assign ramUsageInGB = node.endpointInfo.ramUsageInBytes/1024/1024/1024>
                                    ${node.endpointInfo.ramUsageInPercent}&percnt; (${ramUsageInGB?string["0.##"]} GB)
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <div>
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
                                <td>${node.nodeStats.uptimeFormatted}</td>
                            </tr>
                            <tr>
                                <td>Elasticsearch version:</td>
                                <td>${node.elasticsearchVersion}</td>
                            </tr>
                            <tr>
                                <td>Java version:</td>
                                <td>${node.jvmVersion}</td>
                            </tr>
                            <tr>
                                <td>Heap usage:</td>
                                <td>
                                    <#assign heapUsageInMB = node.nodeStats.heapUsageInBytes/1024/1024>
                                    <#assign maxHeapInMB = node.nodeStats.maximumHeapInBytes/1024/1024>
                                    ${node.nodeStats.heapUsageInPercent}&percnt; (${heapUsageInMB?string["0.##"]} MB / ${maxHeapInMB?string["0.##"]} MB)
                                </td>
                            </tr>
                            <tr>
                                <td>File system:</td>
                                <td>
                                    <#assign freeSpaceOnFileSystemInGB = node.nodeStats.availableBytesOnFileSystem/1024/1024/1024>
                                    ${freeSpaceOnFileSystemInGB?string["0.##"]} GB free
                                </td>
                            </tr>
                            <tr>
                                <td>Documents:</td>
                                <td>
                                    <#assign documentSizeInMB = node.nodeStats.documentSizeInBytes/1024/1024>
                                    ${node.nodeStats.numberOfDocuments} (${documentSizeInMB?string["0.##"]} MB)
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <div>
                        <h3>Node Roles</h3>

                        <table>
                            <#if node.masterNode>
                                <tr>
                                    <td class="centered">&#11088;</td>
                                    <td>This node is the current master node.</td>
                                </tr>
                            </#if>
                            <#if node.masterEligibleNode>
                                <tr>
                                    <td class="centered">&#128172;</td>
                                    <td>This node can become a master node.</td>
                                </tr>
                            </#if>
                            <#if node.dataNode>
                                <tr>
                                    <td class="centered">&#128193;</td>
                                    <td>This node holds search data.</td>
                                </tr>
                            </#if>
                            <#if node.ingestNode>
                                <tr>
                                    <td class="centered">&#128295;</td>
                                    <td>This node can pre-process documents.</td>
                                </tr>
                            </#if>
                        </table>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</#macro>