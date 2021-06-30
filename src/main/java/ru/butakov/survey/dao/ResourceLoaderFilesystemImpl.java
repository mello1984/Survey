package ru.butakov.survey.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.butakov.survey.exceptions.SurveyDaoException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@ConditionalOnProperty(value = "app.filepath", havingValue = "filesystem")
public class ResourceLoaderFilesystemImpl implements ResourceLoader {

    @Override
    public InputStream getInputStream(String filename) {
        try {
            return new BufferedInputStream(Files.newInputStream(Path.of(filename)));
        } catch (IOException e) {
            throw new SurveyDaoException(e);
        }
    }
}
