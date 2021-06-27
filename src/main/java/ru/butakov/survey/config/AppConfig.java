package ru.butakov.survey.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource(value = "classpath:testing.properties"),
        @PropertySource(value = "classpath:application.properties")})
public class AppConfig {
}
