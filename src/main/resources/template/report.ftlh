<#-- @ftlvariable name="report" type="app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport" -->
<#import "macros/styles/common-styles.ftl" as common>
<#import "macros/styles/table-styles.ftl" as tables>
<#import "macros/styles/grid-styles.ftl" as grids>
<#import "macros/styles/detail-section-styles.ftl" as detail_sections>
<#import "macros/sections/configuration-section.ftl" as configuration>
<#import "macros/sections/cluster-info-section.ftl" as cluster_info>
<#import "macros/sections/nodes-info-section.ftl" as nodes_info>
<#import "macros/sections/error-aborted-section.ftl" as error_aborted>
<#import "macros/sections/problems-section.ftl" as problems>
<#import "macros/sections/warnings-section.ftl" as warnings>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Elasticsearch Status Report</title>
    <meta charset="utf-8">
    <@common.style />
    <@tables.style />
    <@grids.style />
    <@detail_sections.style />
</head>
<body>
<h1>Elasticsearch Status Report</h1>

<@configuration.section />

<#if report.reportProgress == "FINISHED">
    <@cluster_info.section />
    <@nodes_info.section />
<#elseif report.reportProgress=="ABORTED">
    <@error_aborted.section />
</#if>

<#if report.problems?size gt 0>
    <@problems.section />
</#if>

<#if report.warnings?size gt 0>
    <@warnings.section />
</#if>

</body>
</html>