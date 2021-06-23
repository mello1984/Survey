package ru.butakov.survey.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

@Repository
public class ClasspathCsvRepository extends AbstractCsvRepository {
    public ClasspathCsvRepository(@Value("${filename}") String filename) {
        super(filename);
    }

    @Override
    InputStream getInputStream(String filename) {
        return this.getClass().getClassLoader().getResourceAsStream(filename);
    }
}
