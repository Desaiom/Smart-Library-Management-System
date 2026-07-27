package com.smartlibrary.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation. Methods annotated with {@code @LogExecutionTime} are
 * intercepted by {@link ExecutionTimeAspect}, which measures and logs how long
 * they take to run. Demonstrates custom annotation-driven AOP.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
}
