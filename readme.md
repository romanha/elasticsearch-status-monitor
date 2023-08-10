# Elasticsearch Status Monitor

[![Build Status](https://app.travis-ci.com/romanha/elasticsearch-status-monitor.svg?branch=develop)](https://app.travis-ci.com/romanha/elasticsearch-status-monitor)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Table of Contents

* [Introduction](#introduction)
* [Usage](#usage)
    * [Requirements](#requirements)
    * [Running the tool](#running-the-tool)
    * [Fallback endpoints](#fallback-endpoints)
    * [Logging](#logging)
* [Configuration](#configuration)
    * [Command line options](#command-line-options)
* [Features](#features)
* [About](#about)
    * [Author](#author)
    * [License](#license)
* [Release notes](#release-notes)

<a id="introduction"></a>
## Introduction

The _Elasticsearch Status Monitor_ is an open-source tool for monitoring an _Elasticsearch_ cluster.

It generates a report containing a cluster overview and potential issues found by its data analysis.

The tool does not manipulate or change the cluster or its data.

<a id="usage"></a>
## Usage

<a id="requirements"></a>
### Requirements

Running the tool requires a Java runtime environment of version 11 or higher.

<a id="running-the-tool"></a>
### Running the tool

You can start the tool by running the command:

```bash
java -jar elasticsearch-status-monitor-[version]-jar-with-dependencies.jar
```

The tool will create a folder called `reports` in the directory from where the JAR is called. This folder includes all generated reports.
Users can customize this path to the reports via configuration.

The report of the most recent run is stored in a folder called `latest`.
Additionally, the tool archives all reports in folders representing the time of the report generation, for example `reports/2021-03-11 22-43-50/index.html`.

To connect to a secured _Elasticsearch_ cluster, make sure that the certificates are added to the trust store of the JRE.
The tool uses the default trust store located at `%JAVA_HOME%/lib/security/cacerts`.

If the JAR is used as part of a script, following exit codes can be used for automation:

| Exit code | Description                                                                        |
|-----------|------------------------------------------------------------------------------------|
| 0         | The analysis was completed and did not find and problems or warnings.              |
| 1         | The analysis was aborted or completed and found problems.                          |
| 2         | The analysis was completed without finding problems, but some warnings were found. |
| 3         | The analysis was not started due to misconfiguration. (e.g. unknown CLI option)    |

<a id="fallback-endpoints"></a>
### Fallback endpoints

An Elasticsearch cluster can consist of multiple nodes, running in a redundant setup.
Therefore, it can happen that some nodes are not running or not reachable, but the cluster as a whole is still functional.

If the tool's main endpoint (`--host` and `--port`) is not reachable, a failing analysis could lead to the wrong assumption that the whole cluster failed.

For such cases you can use fallback endpoints (`--fallbackEndpoints`) to avoid this scenario.
This option allows passing a list of alternative endpoints, which can be used for querying the cluster.

The tool also reports an ["Endpoints not reachable" problem] if one of the endpoints is not part of the cluster.

<a id="logging"></a>
### Logging

The tool logs into a `logs` folder, which is created in the directory from where the tool is called.

The `latest` subdirectory contains the logs of the most recent run.
Additionally, the tool archives logs in subdirectories named after the report's timestamp.

<a id="configuration"></a>
## Configuration

<a id="command-line-options"></a>
### Command line options

The tool supports following CLI options:

| Option                | Alternative | Arguments | Default   | Description                                                                                                | Example                                             |
|-----------------------|-------------|-----------|-----------|------------------------------------------------------------------------------------------------------------|-----------------------------------------------------|
| `--help`              | -           | 0         | -         | Print a help message on how to use this tool.<br/>**By using this option no analysis is started.**         | `--help`                                            |
| `--version`           | -           | 0         | -         | Print the version number of this tool.<br/>**By using this option no analysis is started.**                | `--version`                                         |
| `--host`              | `-h`        | 1         | 127.0.0.1 | The IP address or host name of the Elasticsearch endpoint.                                                 | `--host 127.0.0.1`                                  |
| `--port`              | `-p`        | 1         | 9200      | The HTTP port of the Elasticsearch endpoint.                                                               | `--port 9200`                                       |
| `--fallbackEndpoints` | -           | 1-n       | -         | A list of [fallback endpoints] in the format of `host1:port1,host2:port2`.                                 | `--fallbackEndpoints 127.0.0.1:9202,localhost:9204` |
| `--unsecure`          | -           | 1         | -         | Disables security for the tool. If disabled, the tool will not use HTTPS when connecting to Elasticsearch. | `--unsecure`                                        |
| `--username`          | -           | 1         | admin     | The user name of the Elasticsearch user.                                                                   | `--username admin`                                  |
| `--password`          | -           | 1         | admin     | The password of the Elasticsearch user.                                                                    | `--password admin`                                  |
| `--reportPath`        | -           | 1         | reports   | The path to the location of the generated report files. This can be an absolute or relative path.          | `--reportPath "elasticsearch/reports"`              |
| `--skipArchiveReport` | -           | 0         | -         | Skips the generation of an archive report file located in a timestamp folder.                              | `--skipArchiveReport`                               |

Note that by using one of the help options (`--help`, `--version`) no analysis is started.
These options only print information on how to use this tool.

If any unknown option or invalid argument is passed, no analysis is started.

Here is an example with some options:

```bash
java -jar elasticsearch-status-monitor-[version]-jar-with-dependencies.jar --host 127.0.0.1 --port 9200 --username admin --password admin --reportPath "D:\Elasticsearch\reports\"
```

<a id="features"></a>
## Features

This section lists all issues that the data analysis supports.

<a id="problems"></a>
### Problems

* General connection issues
* Unauthorised connection
* SSL handshake problems
* Cluster not fully operational
* Endpoints not reachable

<a id="warnings"></a>
### Warnings

* Cluster setup is missing redundancy
* Cluster setup allows split brain scenarios (Elasticsearch 6 only)
* Unassigned shards
* High RAM usage on endpoints

<a id="about"></a>
## About

<a id="author"></a>
### Author

The _Elasticsearch Status Monitor_ is developed by Roman Habitzl since December 2020.

<a id="license"></a>
### License

The _Elasticsearch Status Monitor_ is licensed under the [Apache License, Version 2.0].

<a id="release-notes"></a>
## Release notes

The release notes can be found at the [change log].

["Endpoints not reachable" problem]: #problems
[fallback endpoints]: #fallback-endpoints
[Apache License, Version 2.0]: LICENSE
[change log]: changelog.md