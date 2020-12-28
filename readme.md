ELASTICSEARCH-STATUS-MONITOR
============================

This tool provides a quick overview of an _Elasticsearch_ cluster.

It is able to show

* the cluster health status,
* the node and endpoint information.

You can start the tool by running the command `java -jar elasticsearch-status-monitor-[version]-jar-with-dependencies.jar`.

To connect to a secured _Elasticsearch_ cluster, make sure that the certificates are added to the JKS of the JRE.

RELEASE NOTES
=============

1.0.0
-----
**Released on: 2020-not-yet-released**

* Built for: Java 11 / Elasticsearch 6.8.13
* Initial release
  * SSL/TLS support
  * Get cluster health
  * Get node and endpoint information