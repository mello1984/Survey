package ru.butakov.survey.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

@Repository
@ConditionalOnProperty(value = "app.repository", havingValue = "classpathCsvRepository")
public class ClasspathCsvRepository extends AbstractCsvRepository {
    public ClasspathCsvRepository(@Value("${app.filename}") String filename) {
        super(filename);
    }

    @Override
    InputStream getInputStream(String filename) {
        return this.getClass().getClassLoader().getResourceAsStream(filename);
    }
}
