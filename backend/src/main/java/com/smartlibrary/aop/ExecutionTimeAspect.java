package com.smartlibrary.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@code @Around} advice that times the execution of any method annotated with
 * {@link LogExecutionTime}. Demonstrates AOP pointcut binding to a custom
 * annotation and proceeding join points.
 */
@Aspect
@Component
public class ExecutionTimeAspect {

    private static final Logger log = LoggerFactory.getLogger(ExecutionTimeAspect.class);

    @Around("@annotation(com.smartlibrary.aop.LogExecutionTime)")
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long tookMs = (System.nanoTime() - start) / 1_000_000;
            log.info("[timing] {} executed in {} ms",
                    joinPoint.getSignature().toShortString(), tookMs);
        }
    }
}
