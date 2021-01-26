# Elasticsearch Status Monitor

## Table of Contents

* [Introduction](#introduction)
* [Usage](#usage)
* [Configuration](#configuration)
  * [Command Line Options](#command-line-options)
* [Features](#features)
* [About](#about)
  * [Author](#author)
  * [License](#license)
* [Release Notes](#release-notes)

## Introduction

The _Elasticsearch Status Monitor_ is an open-source tool for monitoring an _Elasticsearch_ cluster.

It generates a report containing a cluster overview and potential issues found by its data analysis.

The tool does not manipulate or change the cluster or its data.

## Usage

### Requirements

The tool requires Java 11.

### Running the tool

You can start the tool by running the command:

```bash
java -jar elasticsearch-status-monitor-[version]-jar-with-dependencies.jar
```

The tool will create a folder called `reports` in the directory of the JAR. This folder includes all generated reports.

To connect to a secured _Elasticsearch_ cluster, make sure that the certificates are added to the trust store of the JRE.
The tool uses the default trust store located at `%JAVA_HOME%/lib/security/cacerts`.

If the JAR is used as part of a script, following exit codes can be used for automation:

Exit Code | Description
---       | ---
0         | The analysis was completed and did not find and problems or warnings.
1         | The analysis was aborted or completed and found problems.
2         | The analysis was completed without finding problems, but some warnings were found.
3         | The analysis was not started due to misconfiguration. (e.g. unknown CLI option)

## Configuration

### Command Line Options

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

### Warnings

* Cluster setup is missing redundancy
* Cluster setup allows split brain scenarios
* High RAM usage on endpoints

## About

### Author

The _Elasticsearch Status Monitor_ is developed by Roman Habitzl since December 2020.

### License

To be done.

## Release Notes

### 1.0.0

**Released on: 2021-not-yet-released**

* Built for: Java 11 / Elasticsearch 6.8.13
* Initial release
  * Elasticsearch monitoring
    * Cluster information
    * Node and endpoint information
  * Problem analysis
    * Connection issues
    * Cluster not being redundant
    * Possibility of split brain scenarios
    * High RAM usage on endpoints
  * HTML report generation
  * Help output via CLI options (help, version)
  * Tool configuration via CLI options (host, port, security, username, password)
  * SSL/TLS support for connecting to Elasticsearch
  * Exit codes for running as part of a script
