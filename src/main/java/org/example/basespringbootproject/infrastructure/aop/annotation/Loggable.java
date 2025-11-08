package org.example.basespringbootproject.infrastructure.aop.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Loggable {
    boolean logArgs() default true;

    boolean logReturn() default true;

    boolean logExecutionTime() default true;
}
