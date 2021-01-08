ELASTICSEARCH-STATUS-MONITOR
============================

The _Elasticsearch Status Monitor_ provides a quick overview of an _Elasticsearch_ cluster and analyses the data for potential problems.

It is able to show

* the cluster health status,
* the node and endpoint information.

The automated analysis can find

* connection issues,
* cluster setup issues (redundancy, possibility for split brain scenarios)
* high RAM usage on endpoints.

This tool is developed by Roman Habitzl.

Usage
-----

You can start the tool by running the command `java -jar elasticsearch-status-monitor-[version]-jar-with-dependencies.jar`.

The tool will create a folder called `Elasticsearch Status Reports` in the directory of the JAR. This folder includes all generated reports.

To connect to a secured _Elasticsearch_ cluster, make sure that the certificates are added to the trust store of the JRE.
The tool uses the default trust store located at `%JAVA_HOME%/lib/security/cacerts`.

Command Line Options
--------------------

The tool supports following CLI options:

Option   | Alternative | Arguments | Required | Default   | Description                                                                                                           | Example
---      | ---         | ---       | ---      | ---       | ---                                                                                                                   | ---
h        | host        | 1         | no       | 127.0.0.1 | The IP address or host name of the Elasticsearch endpoint.                                                            | `-h 127.0.0.1`
p        | port        | 1         | no       | 9200      | The HTTP port of the Elasticsearch endpoint.                                                                          | `-p 9200`
s        | security    | 1         | no       | true      | Enables or disables security for the tool. If disabled, the tool will not use HTTPS when connecting to Elasticsearch. | `-s false`
u        | username    | 1         | no       | admin     | The user name of the Elasticsearch user.                                                                              | `-u admin`
x        | password    | 1         | no       | admin     | The password of the Elasticsearch user.                                                                               | `-x admin`

Here is an example with some options:

`java -jar elasticsearch-status-monitor-[version]-jar-with-dependencies.jar -h 127.0.0.1 -p 9200 -u admin -x admin`

This is the same example using the long alternative option names:

`java -jar elasticsearch-status-monitor-[version]-jar-with-dependencies.jar --host 127.0.0.1 --port 9200 --username admin --password admin`

RELEASE NOTES
=============

1.0.0
-----
**Released on: 2020-not-yet-released**

* Built for: Java 11 / Elasticsearch 6.8.13
* Initial release
  * Elasticsearch monitoring
    * Cluster information
    * Node and endpoint information
  * Automatic problem analysis
    * Connection issues
    * Cluster not being redundant
    * Possibility of split brain scenarios
    * High RAM usage on endpoints
  * HTML report generation
  * Tool configuration via CLI options (host, port, security, username, password)
  * SSL/TLS support for connecting to Elasticsearch
