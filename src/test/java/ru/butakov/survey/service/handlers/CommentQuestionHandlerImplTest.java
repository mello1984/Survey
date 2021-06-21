package ru.butakov.survey.service.handlers;

import org.junit.jupiter.api.Test;
import ru.butakov.survey.domain.Answer;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentQuestionHandlerImplTest {
    private final QuestionHandler handler = new CommentQuestionHandlerImpl();

    @Test
    void getQuestionType() {
        assertThat(handler.getQuestionType()).isEqualTo(QuestionType.COMMENT);
    }

    @Test
    void getPoints() {
        int points = 10;
        Question question = new Question(1, QuestionType.COMMENT, "text", points);
        String answer = "Hello world!";
        assertThat(handler.getPoints(question, answer)).isEqualTo(points);
    }

    @Test
    void getAnswersList() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer = new Answer(question, "answer text", true);
        question.addAnswer(answer);

        List<String> actual = handler.getAnswersList(question);
        assertThat(actual).isEqualTo(Collections.emptyList());
    }

    @Test
    void checkQuestion_thenSuccessful() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        boolean actual = handler.checkQuestion(question);
        assertThat(actual).isTrue();
    }

    @Test
    void checkQuestion_thenFalse_notEmptyAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer = new Answer(question, "answer text", true);
        question.addAnswer(answer);

        boolean actual = handler.checkQuestion(question);
        assertThat(actual).isFalse();
    }

    @Test
    void checkQuestion_thenFalse_emptyText() {
        Question question = new Question(1, QuestionType.COMMENT, "", 10);

        boolean actual = handler.checkQuestion(question);
        assertThat(actual).isFalse();
    }

    @Test
    void checkQuestion_thenFalse_blankText() {
        Question question = new Question(1, QuestionType.COMMENT, " ", 10);

        boolean actual = handler.checkQuestion(question);
        assertThat(actual).isFalse();
    }
}