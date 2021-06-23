package ru.butakov.survey.service.handlers;

import org.springframework.stereotype.Service;
import ru.butakov.survey.aop.Loggable;
import ru.butakov.survey.domain.Answer;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.util.ArrayList;
import java.util.List;

@Service
@Loggable
public class RadioQuestionHandler implements QuestionHandler {
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.RADIO_BOX;
    }

    @Override
    public int getPoints(Question question, String answer) {
        int number = Integer.parseInt(answer) - 1;
        return question.getAnswers().get(number).isRight() ? question.getPoints() : 0;
    }

    @Override
    public List<String> getAnswersList(Question question) {
        List<String> result = new ArrayList<>();
        int i = 1;
        for (Answer answer : question.getAnswers()) {
            result.add(String.format("%d. %s", i++, answer.getText()));
        }
        return result;
    }

    @Override
    public boolean checkQuestion(Question question) {
        return !question.getText().isBlank() &&
                question.getAnswers().size() > 1 &&
                question.getAnswers().stream().filter(Answer::isRight).count() == 1 &&
                question.getAnswers().stream().noneMatch(a -> a.getText().isBlank());
    }
}
