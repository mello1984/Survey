package ru.butakov.survey;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import ru.butakov.survey.service.ConsoleServiceImpl;

import java.util.Arrays;

@ComponentScan
@PropertySources({
        @PropertySource(value = "classpath:testing.properties"),
        @PropertySource(value = "classpath:application.properties")})

public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

        System.out.println(Arrays.toString(context.getBeanDefinitionNames()));

        ConsoleServiceImpl handler = context.getBean(ConsoleServiceImpl.class);
        handler.printAllQuestions();
        handler.startTest();
    }
}
