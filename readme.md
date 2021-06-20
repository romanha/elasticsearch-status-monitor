# Elasticsearch Status Monitor

[![Build Status](https://travis-ci.com/romanha/elasticsearch-status-monitor.svg?branch=develop)](https://travis-ci.com/romanha/elasticsearch-status-monitor)
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

Each report is stored in a folder representing the time of the report generation, for example `reports/2021-03-11 22-43-50/index.html`.

To connect to a secured _Elasticsearch_ cluster, make sure that the certificates are added to the trust store of the JRE.
The tool uses the default trust store located at `%JAVA_HOME%/lib/security/cacerts`.

If the JAR is used as part of a script, following exit codes can be used for automation:

Exit code | Description
---       | ---
0         | The analysis was completed and did not find and problems or warnings.
1         | The analysis was aborted or completed and found problems.
2         | The analysis was completed without finding problems, but some warnings were found.
3         | The analysis was not started due to misconfiguration. (e.g. unknown CLI option)

<a id="fallback-endpoints"></a>
### Fallback endpoints

Only because the provided main endpoint (`--host` and `--port`) is not reachable does not mean that the whole cluster is not working.
A failing analysis could lead to the wrong assumption that the whole cluster failed.

You can use fallback endpoints (`--fallbackEndpoints`) to avoid this scenario.
This option allows passing a list of alternative endpoints which can be used for querying the cluster.

The tool tries to connect to each endpoint and reports an ["Endpoints not reachable" problem](#problems) if one of them is not reachable.
It then proceeds to query the cluster on the first reachable endpoint.

<a id="logging"></a>
### Logging

The tool logs into a `logs` folder, which is created in the directory from where the tool is called.

<a id="configuration"></a>
## Configuration

<a id="command-line-options"></a>
### Command line options

The tool supports following CLI options:

Option               | Alternative  | Arguments | Default   | Description                                                                                                | Example
---                  | ---          | ---       | ---       | ---                                                                                                        | ---
`--help`             | -            | 0         | -         | Print a help message on how to use this tool. **By using this option no analysis is started.**             | `--help`
`--version`          | -            | 0         | -         | Print the version number of this tool. **By using this option no analysis is started.**                    | `--version`
`--host`             | `-h`         | 1         | 127.0.0.1 | The IP address or host name of the Elasticsearch endpoint.                                                 | `--host 127.0.0.1`
`--port`             | `-p`         | 1         | 9200      | The HTTP port of the Elasticsearch endpoint.                                                               | `--port 9200`
`--fallbackEndpoints`| -            | n         | -         | A list of [fallback endpoints](#fallback-endpoints) in the format of `host1:port1,host2:port2`.            | `--fallbackEndpoints 127.0.0.1:9202,127.0.0.1:9204`
`--unsecure`         | -            | 1         | -         | Disables security for the tool. If disabled, the tool will not use HTTPS when connecting to Elasticsearch. | `--unsecure`
`--username`         | -            | 1         | admin     | The user name of the Elasticsearch user.                                                                   | `--username admin`
`--password`         | -            | 1         | admin     | The password of the Elasticsearch user.                                                                    | `--password admin`
`--reportPath`       | -            | 1         | reports   | The path to the location of the generated report files. This can be an absolute or relative path.          | `--reportPath "elasticsearch/reports"`

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
* Cluster setup allows split brain scenarios
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

[Apache License, Version 2.0]: LICENSE
[change log]: changelog.md