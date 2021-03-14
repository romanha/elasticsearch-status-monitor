# Elasticsearch Status Monitor

[![Build Status](https://travis-ci.com/romanha/elasticsearch-status-monitor.svg?branch=develop)](https://travis-ci.com/romanha/elasticsearch-status-monitor)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Table of Contents

* [Introduction](#introduction)
* [Usage](#usage)
  * [Requirements](#requirements)
  * [Running the tool](#running-the-tool)
  * [Logging](#logging)
* [Configuration](#configuration)
  * [Command line options](#command-line-options)
* [Features](#features)
* [About](#about)
  * [Author](#author)
  * [License](#license)
* [Release notes](#release-notes)

## Introduction

The _Elasticsearch Status Monitor_ is an open-source tool for monitoring an _Elasticsearch_ cluster.

It generates a report containing a cluster overview and potential issues found by its data analysis.

The tool does not manipulate or change the cluster or its data.

## Usage

### Requirements

Running the tool requires a Java runtime environment of version 11 or higher.

### Running the tool

You can start the tool by running the command:

```bash
java -jar elasticsearch-status-monitor-[version]-jar-with-dependencies.jar
```

The tool will create a folder called `reports` in the directory of the JAR. This folder includes all generated reports.

Each report is stored in a folder representing the time of the report generation, for example `reports/2021-03-11 22-43-50/index.html`.

To connect to a secured _Elasticsearch_ cluster, make sure that the certificates are added to the trust store of the JRE.
The tool uses the default trust store located at `%JAVA_HOME%/lib/security/cacerts`.

If the JAR is used as part of a script, following exit codes can be used for automation:

Exit Code | Description
---       | ---
0         | The analysis was completed and did not find and problems or warnings.
1         | The analysis was aborted or completed and found problems.
2         | The analysis was completed without finding problems, but some warnings were found.
3         | The analysis was not started due to misconfiguration. (e.g. unknown CLI option)

### Logging

The tool logs into a `logs` folder, which is created in the directory from where the tool is called.

## Configuration

### Command line options

The tool supports following CLI options:

Option       | Alternative  | Arguments | Default   | Description                                                                                                | Example
---          | ---          | ---       | ---       | ---                                                                                                        | ---
`--help`     | -            | 0         | -         | Print a help message on how to use this tool. **By using this option no analysis is started.**             | `--help`
`--version`  | -            | 0         | -         | Print the version number of this tool. **By using this option no analysis is started.**                    | `--version`
`--host`     | `-h`         | 1         | 127.0.0.1 | The IP address or host name of the Elasticsearch endpoint.                                                 | `--host 127.0.0.1`
`--port`     | `-p`         | 1         | 9200      | The HTTP port of the Elasticsearch endpoint.                                                               | `--port 9200`
`--unsecure` | -            | 1         | -         | Disables security for the tool. If disabled, the tool will not use HTTPS when connecting to Elasticsearch. | `--unsecure`
`--username` | -            | 1         | admin     | The user name of the Elasticsearch user.                                                                   | `--username admin`
`--password` | -            | 1         | admin     | The password of the Elasticsearch user.                                                                    | `--password admin`

Note that by using one of the help options (`--help`, `--version`) no analysis is started.
These options only print information on how to use this tool.

If any unknown option or invalid argument is passed, no analysis is started.

Here is an example with some options:

```bash
java -jar elasticsearch-status-monitor-[version]-jar-with-dependencies.jar --host 127.0.0.1 --port 9200 --username admin --password admin
```

## Features

This section lists all issues that the data analysis supports.

### Problems

* General connection issues
* Unauthorised connection
* SSL handshake problems
* Cluster not fully operational

### Warnings

* Cluster setup is missing redundancy
* Cluster setup allows split brain scenarios
* Unassigned shards
* High RAM usage on endpoints

## About

### Author

The _Elasticsearch Status Monitor_ is developed by Roman Habitzl since December 2020.

### License

The _Elasticsearch Status Monitor_ is licensed under the [Apache License, Version 2.0].

## Release notes

### 1.1.0

**Not yet released.**

Built for: Java 11 / Elasticsearch 6.8.13

#### Changes

* Renamed the generated HTML report file from `report.html` to `index.html`.
  * This improves usability when running the tool on a webserver.
  
#### New features

* Added the timestamp to the report.
* Problem analysis for "Cluster not fully operational".
  
#### Fixes

* The heap usage was part of the endpoint information in the report.
  * Moved heap usage to node information.
  
#### Miscellaneous

* Updated Google Guice dependency from 5.0.0-BETA-1 to 5.0.1
* Improved styling of the generated HTML report file.

### 1.0.0

**Released on: 2021-03-08**

Built for: Java 11 / Elasticsearch 6.8.13

#### New features

* Elasticsearch monitoring
  * Cluster information
  * Node and endpoint information
* Problem analysis
  * Connection issues
  * Cluster not being redundant
  * Possibility of split brain scenarios
  * Unassigned shards
  * High RAM usage on endpoints
* HTML report generation
* Help output via CLI options (help, version)
* Tool configuration via CLI options (host, port, security, username, password)
* SSL/TLS support for connecting to Elasticsearch
* Exit codes for running the tool as part of a script

[Apache License, Version 2.0]: LICENSE