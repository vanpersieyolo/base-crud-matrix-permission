package org.example.basespringbootproject.infrastructure.aop.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PerformanceTrack {
    long threshold() default 1000; // ms — log warning if execution time exceeds this value (default 1 second)
}