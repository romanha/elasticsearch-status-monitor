package app.habitzl.elasticsearch.status.monitor.presentation.file;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface for injecting the report {@link java.io.File}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@BindingAnnotation
public @interface ReportFile {
	// marker interface for injection
}