package ru.butakov.survey.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Aspect
@Component
public class LoggingAspect {

    @Before("ru.butakov.survey.aop.Pointcuts.loggable()")
    public void loggingBefore(JoinPoint joinPoint) {
        Logger log = getLogger(joinPoint.getTarget().getClass());
        String loggingString = getLoggingString(joinPoint);
        log.info("Logging before: " + loggingString);
    }

    @After("ru.butakov.survey.aop.Pointcuts.loggable()")
    public void loggingAfter(JoinPoint joinPoint) {
        Logger log = getLogger(joinPoint.getTarget().getClass());
        log.info("Logging after: " + getLoggingString(joinPoint));
    }

    private String getLoggingString(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        String args = Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));

        return String.format("method=%s, args=[%s]", method, args);
    }
}
