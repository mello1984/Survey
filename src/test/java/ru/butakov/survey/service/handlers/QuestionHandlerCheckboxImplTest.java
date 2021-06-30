package ru.butakov.survey.service.handlers;

import org.junit.jupiter.api.Test;
import ru.butakov.survey.domain.Answer;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionHandlerCheckboxImplTest {
    private final QuestionHandler handler = new QuestionHandlerCheckboxImpl();

    @Test
    void getQuestionType() {
        assertThat(handler.getQuestionType()).isEqualTo(QuestionType.CHECKBOX);
    }

    @Test
    void getPoints_thenSuccessful_noneAnswer() {
        int points = 10;
        Question question = new Question(1, QuestionType.COMMENT, "text", points);
        Answer answer1 = new Answer(question, "answer text", false);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        String answerString = "";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(points);
    }

    @Test
    void getPoints_thenSuccessful_oneAnswer() {
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
    void getPoints_thenSuccessful_twoAnswer_1() {
        int points = 10;
        Question question = new Question(1, QuestionType.COMMENT, "text", points);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", true);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        String answerString = "1 2";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(points);
    }

    @Test
    void getPoints_thenSuccessful_twoAnswer_2() {
        int points = 10;
        Question question = new Question(1, QuestionType.COMMENT, "text", points);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", true);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        String answerString = "2 1";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(points);
    }

    @Test
    void getPoints_thenWrong_wrongAnswer_1() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        String answerString = "2";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(0);
    }

    @Test
    void getPoints_thenWrong_wrongAnswer_2() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        String answerString = "";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(0);
    }

    @Test
    void getPoints_thenWrong_wrongAnswer_3() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", false);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        String answerString = "1";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(0);
    }

    @Test
    void getPoints_thenWrong_wrongAnswer_4() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", true);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        String answerString = "";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(0);
    }

    @Test
    void getPoints_thenWrong_wrongAnswer_5() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", true);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        String answerString = "1";
        assertThat(handler.getPoints(question, answerString)).isEqualTo(0);
    }

    @Test
    void getPoints_thenWrong_wrongAnswer_6() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", true);
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
        List<String> expected = List.of("1. answer text", "2. answer text 2");
        List<String> actual = handler.getAnswersList(question);
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
    void checkQuestion_thenWrong_emptyText() {
        Question question = new Question(1, QuestionType.COMMENT, "", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenWrong_blankText() {
        Question question = new Question(1, QuestionType.COMMENT, " ", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenWrong_noneAnswers() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenWrong_oneAnswer() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "answer text", true);
        question.addAnswer(answer1);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenWrong_emptyAnswerText() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, "", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }

    @Test
    void checkQuestion_thenWrong_blankAnswerText() {
        Question question = new Question(1, QuestionType.COMMENT, "text", 10);
        Answer answer1 = new Answer(question, " ", true);
        Answer answer2 = new Answer(question, "answer text 2", false);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        assertThat(handler.checkQuestion(question)).isFalse();
    }
}