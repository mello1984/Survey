package ru.butakov.survey.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
@ConditionalOnProperty(value = "app.repository", havingValue = "filesystemCsvRepository")
public class FilesystemCsvRepository extends AbstractCsvRepository {
    public FilesystemCsvRepository(@Value("${app.filename}") String filename) {
        super(filename);
    }

    @Override
    InputStream getInputStream(String filename) {
        try {
            return new BufferedInputStream(Files.newInputStream(Path.of(filename)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
