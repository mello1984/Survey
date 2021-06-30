package ru.butakov.survey.dao;

import org.junit.jupiter.api.Test;
import ru.butakov.survey.exceptions.SurveyDaoException;

import java.io.*;
import java.nio.file.NoSuchFileException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceLoaderFilesystemImplTest {

    @Test
    void getInputStream_thenSuccessful() throws IOException {
        ResourceLoader resourceLoader = new ResourceLoaderFilesystemImpl();
        String separator = File.separator;
        String filename = String.join(separator, "d:", "test", "questions.csv");
        FileInputStream expected = new FileInputStream(filename);
        InputStream actual = resourceLoader.getInputStream(filename);
        assertThat(actual.readAllBytes()).isEqualTo(expected.readAllBytes());
    }

    @Test
    void getInputStream_thenFailed_fileNotExists() {
        ResourceLoader resourceLoader = new ResourceLoaderFilesystemImpl();
        String separator = File.separator;
        String filename = String.join(separator, "d:", "test", "notExistsFile.csv");
        assertThatThrownBy(() -> resourceLoader.getInputStream(filename))
                .isInstanceOf(SurveyDaoException.class)
                .hasMessageContaining(filename)
                .getRootCause()
                .isInstanceOf(NoSuchFileException.class)
                .hasMessageContaining(filename);
    }
}