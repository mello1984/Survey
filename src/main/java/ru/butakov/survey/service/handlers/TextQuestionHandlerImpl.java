package ru.butakov.survey.service.handlers;

import org.springframework.stereotype.Service;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.util.Collections;
import java.util.List;

@Service
public class TextQuestionHandlerImpl implements QuestionHandler {
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TEXT_BOX;
    }

    @Override
    public int getPoints(Question question, String answer) {
        return question.getAnswers().get(0).getText().equals(answer) ? question.getPoints() : 0;
    }

    @Override
    public List<String> getAnswersList(Question question) {
        return Collections.emptyList();
    }

    @Override
    public boolean checkQuestion(Question question) {
        return !question.getText().isBlank() &&
                question.getAnswers().size() == 1 &&
                question.getAnswers().get(0).isRight() &&
                !question.getAnswers().get(0).getText().isBlank();
    }
}
