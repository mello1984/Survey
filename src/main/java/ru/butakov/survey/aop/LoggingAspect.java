package ru.butakov.survey.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("@annotation(ru.butakov.survey.aop.Loggable)")
    public void loggableMethods() {
    }

    @Pointcut("@target(ru.butakov.survey.aop.Loggable)")
    public void loggableClasses() {
    }

    @Before("loggableClasses() || loggableMethods()")
    public void logging(JoinPoint joinPoint) {
        log.info("Logging before execution of method: " +
                joinPoint.getTarget() + "." + joinPoint.getSignature().getName() +
                ", args: " +
                Arrays.toString(joinPoint.getArgs()));
    }

}
