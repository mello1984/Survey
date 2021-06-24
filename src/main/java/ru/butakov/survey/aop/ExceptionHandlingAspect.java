package ru.butakov.survey.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class ExceptionHandlingAspect {

    @AfterThrowing(pointcut = "ru.butakov.survey.aop.Pointcuts.loggable()", throwing = "exception")
    public void loggingThrowing(Exception exception) {
        log.warn("Logging after throwing: " + exception);
    }

}
