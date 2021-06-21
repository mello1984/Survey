package ru.butakov.survey.service.handlers;

import org.junit.jupiter.api.Test;
import ru.butakov.survey.domain.Answer;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RadioQuestionHandlerTest {
    private final QuestionHandler handler = new RadioQuestionHandler();

    @Test
    void getQuestionType() {
        assertThat(handler.getQuestionType()).isEqualTo(QuestionType.RADIO_BOX);
    }

    @Test
    void getPoints_thenSuccessful() {
        int points = 10;
        Question question = new Question(1, QuestionType.COMMENT, "text", points);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        String answerString = "1";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(points);
    }

    @Test
    void getPoints_thenFailed_wrongAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        String answerString = "2";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(0);
    }

    @Test
    void getAnswersList() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        List<String> actual = handler.getAnswersList(question);
        List<String> expected = List.of("1. answer text", "2. answer text 2");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkQuestion_thenSuccessful() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isTrue();
    }

    @Test
    void checkQuestion_thenFailed_emptyQuestionText() {
        Question question = new Question(1, QuestionType.COMMENT, "", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFailed_blankQuestionText() {
        Question question = new Question(1, QuestionType.COMMENT, " ", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFailed_noneAnswers() {
        Question question = new Question(1, QuestionType.COMMENT, " ", 10);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFailed_oneAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, " ", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        question.addAnswer(answer1);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFailed_noneRightAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", false);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFailed_moreRightAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", true);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFailed_emptyAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenFailed_blankAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "  ", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }
}