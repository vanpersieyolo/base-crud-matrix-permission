package org.example.basespringbootproject.infrastructure.aop.logging;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.basespringbootproject.infrastructure.aop.annotation.Loggable;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @PostConstruct
    public void init() {
        log.info("✅ LoggingAspect initialized");
    }

    // Apply to all methods in classes annotated with @RestController, @Service, or @Repository
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || " +
              "within(@org.springframework.stereotype.Service *) || " +
              "within(@org.springframework.stereotype.Repository *)")
    public void appComponents() {
    }

    // Apply to methods annotated with @Loggable
    @Pointcut("@annotation(org.example.basespringbootproject.infrastructure.aop.annotation.Loggable)")
    public void loggableMethods() {

    }

    @Around("appComponents() || loggableMethods()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String traceId = LoggingContext.getTraceId();
        if (traceId == null) {
            traceId = LoggingUtils.generateTraceId();
            LoggingContext.setTraceId(traceId);
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        Loggable annotation = signature.getMethod().getAnnotation(Loggable.class);
        boolean logArgs = annotation == null || annotation.logArgs();
        boolean logReturn = annotation == null || annotation.logReturn();
        boolean logExecutionTime = annotation == null || annotation.logExecutionTime();

        Object[] args = joinPoint.getArgs();

        try {
            if (logArgs) {
                log.info("[{}][ARGUMENTS] {}.{}() args={}", traceId, className, methodName, LoggingUtils.toJson(args));
            }

            Object result = joinPoint.proceed();

            if (logReturn) {
                log.info("[{}] [RETURN] {}.{}() return={}", traceId, className, methodName, LoggingUtils.toJson(result));
            }

            if (logExecutionTime) {
                long duration = System.currentTimeMillis() - startTime;
                log.debug("[{}] [TIME] {}.{}() executed in {} ms", traceId, className, methodName, duration);
            }

            return result;

        } catch (Throwable ex) {
            log.error("[{}] [ERROR] Exception in {}.{}(): {}", traceId, className, methodName, ex.getMessage(), ex);
            throw ex;
        } finally {
            LoggingContext.clear();
        }
    }
}
