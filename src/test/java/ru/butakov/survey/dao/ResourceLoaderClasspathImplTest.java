package ru.butakov.survey.dao;

import org.junit.jupiter.api.Test;
import ru.butakov.survey.exceptions.SurveyDaoException;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceLoaderClasspathImplTest {
    ResourceLoader resourceLoader = new ResourceLoaderClasspathImpl();

    @Test
    void getInputStream_thenSuccessful() throws IOException {
        String filename = "questions.csv";
        InputStream expected = resourceLoader.getClass().getClassLoader().getResourceAsStream(filename);
        InputStream actual = resourceLoader.getInputStream(filename);

        assert expected != null;
        assert actual != null;
        assertThat(actual.readAllBytes()).isEqualTo(expected.readAllBytes());
    }

    @Test
    void getInputStream_thenFailed_notFound() {
        String filename = "notExistsFile";
        assertThatThrownBy(() -> resourceLoader.getInputStream(filename))
                .isInstanceOf(SurveyDaoException.class)
                .hasMessage("File " + filename + " not found on classpath");
    }
}