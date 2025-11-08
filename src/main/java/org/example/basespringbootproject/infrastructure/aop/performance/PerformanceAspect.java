package org.example.basespringbootproject.infrastructure.aop.performance;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.basespringbootproject.infrastructure.aop.annotation.PerformanceTrack;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {

    @Pointcut("@annotation(org.example.basespringbootproject.infrastructure.aop.annotation.PerformanceTrack)")
    public void trackPerformance() {
    }

    @Around("trackPerformance()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getDeclaringType().getSimpleName() + "." + methodSignature.getName();

        PerformanceTrack annotation = methodSignature.getMethod().getAnnotation(PerformanceTrack.class);
        long threshold = annotation.threshold();

        if (duration > threshold) {
            log.warn("⚠️ {} executed in {} ms (threshold={} ms)", methodName, duration, threshold);
        } else {
            log.debug("⏱ {} executed in {} ms", methodName, duration);
        }

        return result;
    }
}
