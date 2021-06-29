package ru.butakov.survey.service.utils;

import org.springframework.stereotype.Component;
import ru.butakov.survey.config.TestingPointsProps;
import ru.butakov.survey.dao.QuestionRepository;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;
import ru.butakov.survey.domain.User;
import ru.butakov.survey.service.handlers.QuestionHandler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ConsoleServiceUtils {
    final QuestionRepository repository;
    final Map<QuestionType, QuestionHandler> handlerMap;
    final TestingPointsProps testingPointsProps;

    public ConsoleServiceUtils(QuestionRepository repository,
                               List<QuestionHandler> questionHandlers,
                               TestingPointsProps testingPointsProps) {
        this.repository = repository;
        this.handlerMap = questionHandlers.stream().collect(Collectors.toMap(QuestionHandler::getQuestionType, h -> h));
        this.testingPointsProps = testingPointsProps;
    }

    public User getUser(String username) {
        return User.builder().username(username).build();
    }

    public String testToString() {
        List<Question> questions = repository.findAll();
        String stars = "***************************************************************************************";
        List<String> result = new ArrayList<>();
        result.add(stars);
        result.add("Print questions");
        result.add(stars);
        questions.forEach(q -> result.add(q.toString()));
        result.add(stars);
        return String.join("\n", result);
    }

    public int passTest() {
        int points = 0;
        Scanner scanner = new Scanner(System.in);

        List<Question> questions = repository.findAll();
        for (Question question : questions) {
            System.out.println(question.getText());
            QuestionHandler handler = handlerMap.get(question.getType());
            handler.getAnswersList(question).forEach(System.out::println);
            String answer = scanner.next();
            points += handler.getPoints(question, answer);
        }
        return points;
    }

    public String getTestResultString(User user) {
        int points = passTest();
        return MessageFormat.format("{0}, test completed{3}Minimal points - {1}{3}Your points - {2}{3}",
                user.getUsername(), testingPointsProps.getMin(), points, System.lineSeparator());
    }
}
