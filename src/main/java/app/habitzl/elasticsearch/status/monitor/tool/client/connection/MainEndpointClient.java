package app.habitzl.elasticsearch.status.monitor.tool.client.connection;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface for the Elasticsearch REST client
 * of the preferred endpoint to perform the monitoring against.
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MainEndpointClient {
}
