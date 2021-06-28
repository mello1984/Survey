package ru.butakov.survey.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class AppProps {
    String filename;
}
