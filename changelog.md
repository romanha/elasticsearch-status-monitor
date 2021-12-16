# Change Log

## Releases

* [1.2.0](#120)
* [1.1.0](#110)
* [1.0.0](#100)

<a id="120"></a>
## 1.2.0

Not yet released.

### Compatibility

* Java 11
* Elasticsearch 6.8

### New features

* Added menu to the generated HTML report file.
* Added generating an HTML report file to a `latest` folder within the configured report path.
  * This improves the usability when running the tool on a webserver.
* Added the CLI option `skipArchiveReport` to disable the generation of an archive report file.
* Added logging of the most recent report to a `latest` folder.

### Fixes

* Mitigated the Log4Shell remote code execution vulnerability in the log4j library. (CVE-2021-44228)
    * Updated log4j from 2.14.1 to 2.16.0.

### Miscellaneous

* Added information about the tool to the generated HTML report file.
* Improved styling of the generated HTML report file.
* Updated various dependencies to their latest version.
    * Updated Jackson from 2.12.0 to 2.13.0.
    * Updated JSON Path from 2.5.0 to 2.6.0.
    * Updated Apache Freemarker from 2.3.30 to 2.3.31.
    * Updated SL4J from 1.7.30 to 1.7.32.


<a id="110"></a>
## 1.1.0

Released on: 2021-11-10

### Compatibility

* Java 11
* Elasticsearch 6.8

### Changes

* Renamed the generated HTML report file from `report.html` to `index.html`.
    * This improves the usability when running the tool on a webserver.

### New features

* Added the timestamp to the report.
* Added the CLI option `reportPath` to configure the location of the generated report files.
* Added the actual byte usage to the percentage of used RAM and heap memory.
* Added the operating system name to the endpoint data.
* Added the HTTP publish address to the endpoint data.
* Added the Elasticsearch version to the node data.
* Added the JVM version to the node data.
* Added the "ingest" role to the node data (pre-processing documents).
* Added the available free space on the file system to the node data.
* Added the "Cluster not fully operational" problem analysis.
* Added the CLI option `fallbackEndpoints` to configure a list of alternative endpoint in case of connection problems.
* Added the "Endpoints not reachable" problem analysis.

### Fixes

* The heap usage was part of the endpoint information section in the report.
    * Moved the heap usage to the node information section.
* Added a missing logging binding. (SLF4J to log4j)

### Miscellaneous

* Replaced calls to the Elasticsearch `_cat` API with proper API endpoints. (`_cluster/state`, `_nodes` and `_nodes/stats`)
* Updated Google Guice dependency from `5.0.0-BETA-1` to `5.0.1`.
* Improved styling of the generated HTML report file.


<a id="100"></a>
## 1.0.0

Released on: 2021-03-08

### Compatibility

* Java 11
* Elasticsearch 6.8

### New features

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
