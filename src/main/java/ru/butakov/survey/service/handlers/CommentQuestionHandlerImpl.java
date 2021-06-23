package ru.butakov.survey.service.handlers;

import org.springframework.stereotype.Service;
import ru.butakov.survey.aop.Loggable;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.util.Collections;
import java.util.List;

@Service
@Loggable
public class CommentQuestionHandlerImpl implements QuestionHandler {
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.COMMENT;
    }

    @Override
    public int getPoints(Question question, String answer) {
        return question.getPoints();
    }

    @Override
    public List<String> getAnswersList(Question question) {
        return Collections.emptyList();
    }

    @Override
    public boolean checkQuestion(Question question) {
        return !question.getText().isBlank() && question.getAnswers().isEmpty();
    }
}
