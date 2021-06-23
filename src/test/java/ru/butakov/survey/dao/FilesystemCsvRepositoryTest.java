package ru.butakov.survey.dao;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.NoSuchFileException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FilesystemCsvRepositoryTest {
    private final FilesystemCsvRepository repository = new FilesystemCsvRepository("");

    @Test
    void getInputStream() throws IOException {
        String separator = File.separator;
        String filename = String.join(separator, "d:", "test", "questions.csv");
        FileInputStream expected = new FileInputStream(filename);
        InputStream actual = repository.getInputStream(filename);
        assertThat(actual.readAllBytes()).isEqualTo(expected.readAllBytes());
    }

    @Test
    void getInputStream_thenFailed_fileNotExists() {
        String separator = File.separator;
        String filename = String.join(separator, "d:", "test", "notExistsFile.csv");
        assertThatThrownBy(() -> repository.getInputStream(filename))
                .isInstanceOf(UncheckedIOException.class)
                .hasMessageContaining(filename)
                .getRootCause()
                .isInstanceOf(NoSuchFileException.class)
                .hasMessageContaining(filename);
    }
}