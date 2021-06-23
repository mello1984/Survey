package ru.butakov.survey.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.butakov.survey.dao.QuestionRepository;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;
import ru.butakov.survey.domain.User;
import ru.butakov.survey.service.handlers.QuestionHandler;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsoleServiceImpl {

    QuestionRepository repository;
    Map<QuestionType, QuestionHandler> handlerMap;
    int minPoints;

    public ConsoleServiceImpl(@Qualifier("questionRepository") QuestionRepository repository,
                              List<QuestionHandler> questionHandlers,
                              @Value("${points.min}") int minPoints) {
        this.repository = repository;
        this.handlerMap = questionHandlers.stream().collect(Collectors.toMap(QuestionHandler::getQuestionType, h -> h));
        this.minPoints = minPoints;
    }

    public void startTest() {
        System.out.println("***************************************************************************************");
        System.out.println("Start test on console");
        System.out.println("***************************************************************************************");

        Scanner scanner = new Scanner(System.in);
        User user = new User();

//        System.out.println("Write your name");
//        user.setName(scanner.next());
//        System.out.println("Write your surname");
//        user.setSurname(scanner.next());

        List<Question> questions = repository.findAll();
        for (Question question : questions) {
            System.out.println(question.getText());
            QuestionHandler handler = handlerMap.get(question.getType());
            handler.getAnswersList(question).forEach(System.out::println);
            String answer = scanner.next();
            int points = handler.getPoints(question, answer);
            user.setPoints(user.getPoints() + points);
        }
        System.out.printf("Minimal points - %d\nYour points - %d", minPoints, user.getPoints());
    }

    public void printAllQuestions() {
        System.out.println("***************************************************************************************");
        System.out.println("Print questions");
        System.out.println("***************************************************************************************");
        List<Question> questions = repository.findAll();
        questions.forEach(q -> {
            System.out.println(q.getId() + ", " + q.getType() + ", " + q.getText() + ", " + q.getPoints());
            q.getAnswers().forEach(System.out::println);
            System.out.println();
        });
        System.out.println("***************************************************************************************");
    }

}
