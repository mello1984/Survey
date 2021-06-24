package ru.butakov.survey.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("ru.butakov.survey.aop.Pointcuts.loggable()")
    public void loggingBefore(JoinPoint joinPoint) {
        String args = Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));

        log.info("Logging before: " + joinPoint.toString() + ", args=[" + args + "]");
    }

    @After("ru.butakov.survey.aop.Pointcuts.loggable()")
    public void loggingAfter(JoinPoint joinPoint) {
        log.info("Logging after: " + joinPoint.toString());
    }
}
