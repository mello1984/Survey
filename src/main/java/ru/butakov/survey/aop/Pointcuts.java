package ru.butakov.survey.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(public !static * *(..))")
    public void notStatic() {
    }

    @Pointcut("@annotation(ru.butakov.survey.aop.Loggable)")
    public void loggableMethods() {
    }

    @Pointcut("@within(ru.butakov.survey.aop.Loggable)")
    public void loggableClasses() {
    }

    @Pointcut("ru.butakov.survey.aop.Pointcuts.loggableClasses() || " +
            "ru.butakov.survey.aop.Pointcuts.loggableMethods()")
    public void loggable() {
    }
}
