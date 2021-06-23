package ru.butakov.survey;

import org.springframework.context.annotation.*;
import ru.butakov.survey.service.ConsoleServiceImpl;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan
@PropertySources({
        @PropertySource(value = "classpath:testing.properties"),
        @PropertySource(value = "classpath:application.properties")})

public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

        ConsoleServiceImpl handler = context.getBean(ConsoleServiceImpl.class);
        handler.printAllQuestions();
        handler.startTest();
    }
}
