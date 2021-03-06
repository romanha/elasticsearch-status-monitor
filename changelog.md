# Change Log

## Releases

* [1.1.0](#110)
* [1.0.0](#100)

<a id="110"></a>
## 1.1.0

Not yet released.

### Compatibility

* Java 11
* Elasticsearch 6.8

### Changes

* Renamed the generated HTML report file from `report.html` to `index.html`.
    * This improves usability when running the tool on a webserver.
  
### New features

* Added the timestamp to the report.
* Added the CLI option `reportPath` to configure the location of the generated report files.
* Added the actual byte usage to the percentage of used RAM and heap memory.
* Added the operating system name to the endpoint data.
* Added the Elasticsearch version to the node data.
* Added the JVM version to the node data.
* Added the "ingest" role to the node data (pre-processing documents).
* Added the available free space on the file system to the node data.
* Added the problem analysis for "Cluster not fully operational".
  
### Fixes

* The heap usage was part of the endpoint information in the report.
    * Moved heap usage to node information.
  
### Miscellaneous

* Replaced calls to the Elasticsearch `_cat` API with proper API endpoints. (`_cluster/state`, `_nodes` and `_nodes/stats`)
* Updated Google Guice dependency from 5.0.0-BETA-1 to 5.0.1
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
