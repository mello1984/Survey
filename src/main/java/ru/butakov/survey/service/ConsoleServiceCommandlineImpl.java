package ru.butakov.survey.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.butakov.survey.domain.User;
import ru.butakov.survey.service.utils.ConsoleServiceUtils;

import java.util.Scanner;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConditionalOnProperty(value = "app.consoleService.enabled", havingValue = "true")
@RequiredArgsConstructor
public class ConsoleServiceCommandlineImpl implements CommandLineRunner, ConsoleService {
    final ConsoleServiceUtils consoleServiceUtils;
    User user;

    @Override
    public void startTest() {
        if (isLoggedUser())
            System.out.println(consoleServiceUtils.getTestResultString(user));
    }

    @Override
    public void print() {
        System.out.println(consoleServiceUtils.testToString());
    }

    @Override
    public void login(String username) {
        user = consoleServiceUtils.getUser(username);
        System.out.println("Logged as " + user.getUsername());
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        print();
        System.out.println("Write your username please");
        login(scanner.next());
        startTest();
    }

    boolean isLoggedUser() {
        return user != null;
    }
}
