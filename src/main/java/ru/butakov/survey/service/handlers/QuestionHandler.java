package ru.butakov.survey.service.handlers;

import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.util.List;

public interface QuestionHandler {

    QuestionType getQuestionType();

    int getPoints(Question question, String answer);

    List<String> getAnswersList(Question question);

    boolean checkQuestion(Question question);

}
