package ru.butakov.survey.service.handlers;

import org.junit.jupiter.api.Test;
import ru.butakov.survey.domain.Answer;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TextQuestionHandlerImplTest {
    private final QuestionHandler handler = new TextQuestionHandlerImpl();

    @Test
    void getQuestionType() {
        assertThat(handler.getQuestionType()).isEqualTo(QuestionType.TEXT_BOX);
    }

    @Test
    void getPoints_thenSuccessful() {
        int points = 10;
        Question question = new Question(1, QuestionType.COMMENT, "text", points);
        Answer answer = new Answer(question, "answer text", true);
        question.addAnswer(answer);
        String answerString = "answer text";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(points);
    }

    @Test
    void getPoints_thenReturnZero_wrongAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer = new Answer(question, "answer text", true);
        question.addAnswer(answer);
        String answerString = "Hello world!";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(0);
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
        Answer answer = new Answer(question, "answer text", true);
        question.addAnswer(answer);
        assertThat(handler.checkQuestion(question)).isTrue();
    }


    @Test
    void checkQuestion_thenFalse_emptyQuestionText() {
        Question question = new Question(1, QuestionType.COMMENT, "", 10);
        Answer answer = new Answer(question, "answer text", true);
        question.addAnswer(answer);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFalse_blankQuestionText() {
        Question question = new Question(1, QuestionType.COMMENT, " ", 10);
        Answer answer = new Answer(question, "answer text", true);
        question.addAnswer(answer);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFalse_noAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, " ", 10);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFalse_moreAnswers() {
        Question question = new Question(1, QuestionType.COMMENT, " ", 10);
        Answer answer = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text2", true);
        question.addAnswer(answer);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFalse_falseAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer = new Answer(question, "answer text", false);
        question.addAnswer(answer);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFalse_emptyAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer = new Answer(question, "", true);
        question.addAnswer(answer);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFalse_blankAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer = new Answer(question, "   ", true);
        question.addAnswer(answer);
        assertThat(handler.checkQuestion(question)).isFalse();
    }
}