package ru.butakov.survey.service.handlers;

import org.springframework.stereotype.Service;
import ru.butakov.survey.domain.Answer;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CheckboxQuestionHandler implements QuestionHandler {
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }

    @Override
    public int getPoints(Question question, String answer) {
        Set<Integer> actual = answer.isEmpty() ?
                Collections.emptySet() :
                Arrays.stream(answer.split(" "))
                        .mapToInt(s -> Integer.parseInt(s) - 1)
                        .boxed()
                        .collect(Collectors.toSet());

        Set<Integer> expected = new HashSet<>();
        for (int i = 0; i < question.getAnswers().size(); i++) {
            if (question.getAnswers().get(i).isRight()) expected.add(i);
        }
        return actual.equals(expected) ? question.getPoints() : 0;
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
                question.getAnswers().stream().noneMatch(a -> a.getText().isBlank());
    }
}
