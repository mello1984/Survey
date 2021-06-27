package ru.butakov.survey;

import org.springframework.boot.SpringApplication;
import ru.butakov.survey.service.ConsoleServiceImpl;

public class SpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(ConsoleServiceImpl.class);
    }
}
