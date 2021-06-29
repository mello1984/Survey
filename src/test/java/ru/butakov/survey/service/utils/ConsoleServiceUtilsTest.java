package ru.butakov.survey.service.utils;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.butakov.survey.config.TestingPointsProps;
import ru.butakov.survey.dao.QuestionRepository;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;
import ru.butakov.survey.domain.User;
import ru.butakov.survey.service.handlers.QuestionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConsoleServiceUtilsTest {
    @Mock
    private QuestionRepository repository;
    @Mock
    private Map<QuestionType, QuestionHandler> handlerMap;
    @Mock
    private TestingPointsProps testingPointsProps;

    @Test
    void getUser() {
        ConsoleServiceUtils utils = new ConsoleServiceUtils(repository, Collections.emptyList(), testingPointsProps);
        String expected = "username";
        User user = utils.getUser(expected);
        assertThat(user.getUsername()).isEqualTo(expected);
    }

    @Test
    void testToString() {
        ConsoleServiceUtils utils = new ConsoleServiceUtils(repository, Collections.emptyList(), testingPointsProps);
        Question question1 = new Question(1, QuestionType.COMMENT, "text1", 1);
        Question question2 = new Question(2, QuestionType.COMMENT, "text2", 2);

        List<Question> questions = List.of(question1, question2);
        Mockito.when(repository.findAll()).thenReturn(questions);

        String actual = utils.testToString();
        assertThat(actual).contains(question1.toString());
        assertThat(actual).contains(question2.toString());
    }

    @Test
    @Disabled
    void passTest() {
    }

    @Test
    void getTestResultString() {
        ConsoleServiceUtils utils = Mockito.spy(new ConsoleServiceUtils(repository, Collections.emptyList(), testingPointsProps));
        int points = 100;
        int minValue = 5;
        String username = "Mike";

        Mockito.doReturn(points).when(utils).passTest();
        Mockito.when(testingPointsProps.getMin()).thenReturn(minValue);

        String expected = username + ", test completed" + System.lineSeparator() +
                "Minimal points - " + minValue + System.lineSeparator() +
                "Your points - " + points + System.lineSeparator();

        User user = User.builder().username(username).build();
        String actual = utils.getTestResultString(user);

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(utils).passTest();
        Mockito.verify(utils).getTestResultString(user);
        Mockito.verifyNoMoreInteractions(utils);

        Mockito.verify(testingPointsProps).getMin();
        Mockito.verifyNoMoreInteractions(testingPointsProps);
    }
}