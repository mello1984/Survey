package ru.butakov.survey.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@ConditionalOnProperty(value = "app.filepath", havingValue = "classpath")
public class ClasspathResourceLoaderImpl implements ResourceLoader {

    @Override
    public InputStream getInputStream(String filename) {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filename);
        if (resourceAsStream == null) {
            throw new IllegalArgumentException(String.format("File %s not found on classpath", filename));
        }
        return resourceAsStream;
    }
}
