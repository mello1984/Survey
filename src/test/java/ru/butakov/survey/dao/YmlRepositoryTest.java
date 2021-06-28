package ru.butakov.survey.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.butakov.survey.config.AppProps;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class YmlRepositoryTest {
    @Mock
    ResourceLoader resourceLoader;
    @Mock
    AppProps appProps;

    @Test
    void findAll() {
        YmlRepository repository = new YmlRepository(resourceLoader, appProps);
        Question question = new Question(3, QuestionType.COMMENT, "Comment text", 1);
        List<Question> expected = List.of(question);
        String ymlString = "---\n" +
                "answers: []\n" +
                "id: 3\n" +
                "points: 1\n" +
                "text: Comment text\n" +
                "type: COMMENT";
        InputStream inputStream = new ByteArrayInputStream(ymlString.getBytes());

        Mockito.when(appProps.getFilename()).thenReturn("filename");
        Mockito.when(resourceLoader.getInputStream(appProps.getFilename())).thenReturn(inputStream);

        List<Question> actual = repository.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findById_thenSuccessful() {
        YmlRepository repository = new YmlRepository(resourceLoader, appProps);
        Question expected = new Question(3, QuestionType.COMMENT, "Comment text", 1);
        String ymlString = "---\n" +
                "answers: []\n" +
                "id: 3\n" +
                "points: 1\n" +
                "text: Comment text\n" +
                "type: COMMENT";
        InputStream inputStream = new ByteArrayInputStream(ymlString.getBytes());

        Mockito.when(appProps.getFilename()).thenReturn("filename");
        Mockito.when(resourceLoader.getInputStream(appProps.getFilename())).thenReturn(inputStream);

        Question actual = repository.findById(3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findById_thenNull() {
        YmlRepository repository = new YmlRepository(resourceLoader, appProps);
        String ymlString = "---\n" +
                "answers: []\n" +
                "id: 3\n" +
                "points: 1\n" +
                "text: Comment text\n" +
                "type: COMMENT";
        InputStream inputStream = new ByteArrayInputStream(ymlString.getBytes());

        Mockito.when(appProps.getFilename()).thenReturn("filename");
        Mockito.when(resourceLoader.getInputStream(appProps.getFilename())).thenReturn(inputStream);

        Question actual = repository.findById(1);
        assertThat(actual).isEqualTo(null);
    }
}