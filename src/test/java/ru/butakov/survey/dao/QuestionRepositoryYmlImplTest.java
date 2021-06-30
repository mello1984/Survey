package ru.butakov.survey.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.butakov.survey.config.AppProps;
import ru.butakov.survey.domain.Answer;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class QuestionRepositoryYmlImplTest {
    @Mock
    ResourceLoader resourceLoader;
    @Mock
    AppProps appProps;

    @Test
    void findAll() {
        QuestionRepositoryYmlImpl repository = new QuestionRepositoryYmlImpl(resourceLoader, appProps);
        Question question1 = new Question(1, QuestionType.COMMENT, "Comment text", 1);
        Question question2 = new Question(2, QuestionType.TEXT_BOX, "Open text", 20);
        Answer answer = new Answer(question2, "45", true);
        question2.addAnswer(answer);
        List<Question> expected = List.of(question1, question2);

        String ymlString = "---" + System.lineSeparator() +
                "answers: []" + System.lineSeparator() +
                "id: 1" + System.lineSeparator() +
                "points: 1" + System.lineSeparator() +
                "text: Comment text" + System.lineSeparator() +
                "type: COMMENT" + System.lineSeparator() +
                "---" + System.lineSeparator() +
                "answers:" + System.lineSeparator() +
                "  - right: true" + System.lineSeparator() +
                "    text: '45'" + System.lineSeparator() +
                "id: 2" + System.lineSeparator() +
                "points: 20" + System.lineSeparator() +
                "text: Open text" + System.lineSeparator() +
                "type: TEXT_BOX";

        InputStream inputStream = new ByteArrayInputStream(ymlString.getBytes());

        Mockito.when(appProps.getFilename()).thenReturn("filename");
        Mockito.when(resourceLoader.getInputStream(appProps.getFilename())).thenReturn(inputStream);

        List<Question> actual = repository.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findById_thenSuccessful_whenNoAnswer() {
        QuestionRepositoryYmlImpl repository = new QuestionRepositoryYmlImpl(resourceLoader, appProps);
        Question expected = new Question(3, QuestionType.COMMENT, "Comment text", 1);
        String ymlString = "---" + System.lineSeparator() +
                "answers: []" + System.lineSeparator() +
                "id: 3" + System.lineSeparator() +
                "points: 1" + System.lineSeparator() +
                "text: Comment text" + System.lineSeparator() +
                "type: COMMENT";
        InputStream inputStream = new ByteArrayInputStream(ymlString.getBytes());

        Mockito.when(appProps.getFilename()).thenReturn("filename");
        Mockito.when(resourceLoader.getInputStream(appProps.getFilename())).thenReturn(inputStream);

        Question actual = repository.findById(3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findById_thenSuccessful_whenHasAnswers() {
        QuestionRepositoryYmlImpl repository = new QuestionRepositoryYmlImpl(resourceLoader, appProps);

        Question expected = new Question(3, QuestionType.TEXT_BOX, "Open text", 20);
        Answer answer = new Answer(expected, "45", true);
        expected.addAnswer(answer);

        String ymlString =
                "---" + System.lineSeparator() +
                        "answers:" + System.lineSeparator() +
                        "  - right: true" + System.lineSeparator() +
                        "    text: '45'" + System.lineSeparator() +
                        "id: 3" + System.lineSeparator() +
                        "points: 20" + System.lineSeparator() +
                        "text: Open text" + System.lineSeparator() +
                        "type: TEXT_BOX";
        InputStream inputStream = new ByteArrayInputStream(ymlString.getBytes());

        Mockito.when(appProps.getFilename()).thenReturn("filename");
        Mockito.when(resourceLoader.getInputStream(appProps.getFilename())).thenReturn(inputStream);

        Question actual = repository.findById(3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findById_thenNull() {
        QuestionRepositoryYmlImpl repository = new QuestionRepositoryYmlImpl(resourceLoader, appProps);
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