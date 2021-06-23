package ru.butakov.survey.dao;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class ClasspathCsvRepositoryTest {
    private final ClasspathCsvRepository repository = new ClasspathCsvRepository("");

    @Test
    void getInputStream() throws IOException {
        String filename = "questions.csv";
        InputStream expected = repository.getClass().getClassLoader().getResourceAsStream(filename);
        InputStream actual = repository.getInputStream(filename);

        assert expected != null;
        assert actual != null;
        assertThat(actual.readAllBytes()).isEqualTo(expected.readAllBytes());
    }
}