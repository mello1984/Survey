package ru.butakov.survey.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import ru.butakov.survey.config.TestingPoints;
import ru.butakov.survey.dao.QuestionRepository;
import ru.butakov.survey.domain.Question;
import ru.butakov.survey.domain.QuestionType;
import ru.butakov.survey.domain.User;
import ru.butakov.survey.service.handlers.QuestionHandler;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsoleServiceImpl implements CommandLineRunner {

    QuestionRepository repository;
    Map<QuestionType, QuestionHandler> handlerMap;
    TestingPoints testingPoints;


    public ConsoleServiceImpl(QuestionRepository repository,
                              List<QuestionHandler> questionHandlers,
                              TestingPoints testingPoints) {
        this.repository = repository;
        this.handlerMap = questionHandlers.stream().collect(Collectors.toMap(QuestionHandler::getQuestionType, h -> h));
        this.testingPoints = testingPoints;
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
        System.out.printf("Minimal points - %d\nYour points - %d", testingPoints.getMin(), user.getPoints());
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

    @Override
    public void run(String... args) {
        printAllQuestions();
//        startTest();
    }

    void yamlDumper(Object object) {
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(object, writer);
        System.out.println(writer.toString());
    }
}
