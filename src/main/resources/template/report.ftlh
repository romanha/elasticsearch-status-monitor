<#-- @ftlvariable name="report" type="app.habitzl.elasticsearch.status.monitor.tool.analysis.data.AnalysisReport" -->
<#import "macros/styles/common-styles.ftl" as common>
<#import "macros/styles/menu-styles.ftl" as menu>
<#import "macros/styles/table-styles.ftl" as tables>
<#import "macros/styles/grid-styles.ftl" as grids>
<#import "macros/styles/detail-section-styles.ftl" as detail_sections>
<#import "macros/scripts/menu-scripts.ftl" as menu_scripts>
<#import "macros/sections/menu-section.ftl" as menubar>
<#import "macros/sections/error-aborted-section.ftl" as error_aborted>
<#import "macros/sections/configuration-section.ftl" as configuration>
<#import "macros/sections/no-issues-section.ftl" as no_issues>
<#import "macros/sections/problems-section.ftl" as problems>
<#import "macros/sections/warnings-section.ftl" as warnings>
<#import "macros/sections/cluster-info-section.ftl" as cluster_info>
<#import "macros/sections/nodes-info-section.ftl" as nodes_info>
<#import "macros/sections/indices-section.ftl" as indices>
<#import "macros/sections/about-section.ftl" as about>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Elasticsearch Status Report</title>
    <meta charset="utf-8">
    <@common.style />
    <@menu.style />
    <@tables.style />
    <@grids.style />
    <@detail_sections.style />

    <@menu_scripts.script />
</head>
<body>
<h1 class="centered">Elasticsearch Status Report</h1>

<@menubar.section />

<#if report.reportProgress == "ABORTED">
    <@error_aborted.section />
</#if>

<div id="cluster-section" class="main-section">
    <#if report.reportProgress == "FINISHED">
        <@cluster_info.section />
    </#if>
</div>

<div id="nodes-section" class="main-section hidden">
    <#if report.reportProgress == "FINISHED">
        <@nodes_info.section />
    </#if>
</div>

<div id="indices-section" class="main-section hidden">
    <#if report.reportProgress == "FINISHED">
        <@indices.section />
    </#if>
</div>

<div id="analysis-section" class="main-section hidden">
    <#if report.problems?size gt 0>
        <@problems.section />
    </#if>

    <#if report.warnings?size gt 0>
        <@warnings.section />
    </#if>

    <#if report.reportProgress == "FINISHED" && report.problems?size == 0 && report.warnings?size == 0>
        <@no_issues.section />
    </#if>
</div>

<div id="about-section" class="main-section hidden">
    <div id="about-section-wrapper" class="about-grid-container">
        <div class="about-grid-item">
            <!-- Empty -->
        </div>
        <div class="about-grid-item">
            <@configuration.section />
        </div>
        <div class="about-grid-item">
            <@about.section />
        </div>
        <div class="about-grid-item">
            <!-- Empty -->
        </div>
    </div>
</div>

</body>
</html>