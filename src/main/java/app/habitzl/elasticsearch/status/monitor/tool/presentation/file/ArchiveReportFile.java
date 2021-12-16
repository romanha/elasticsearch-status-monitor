package app.habitzl.elasticsearch.status.monitor.tool.presentation.file;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface for injecting the archive report {@link java.io.File}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@BindingAnnotation
public @interface ArchiveReportFile {
    // marker interface for injection
}
