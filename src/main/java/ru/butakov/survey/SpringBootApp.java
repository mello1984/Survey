package ru.butakov.survey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.butakov.survey.config.AppProps;
import ru.butakov.survey.config.TestingPoints;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties({TestingPoints.class, AppProps.class})
public class SpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class);
    }
}
