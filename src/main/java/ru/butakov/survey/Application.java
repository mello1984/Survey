package ru.butakov.survey;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import ru.butakov.survey.service.ConsoleServiceImpl;

@ComponentScan
@PropertySource(value = "classpath:testing.properties")
public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

        ConsoleServiceImpl handler = context.getBean(ConsoleServiceImpl.class);
        handler.printAllQuestions();
        handler.startTest();

    }

}
