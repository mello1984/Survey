package ru.butakov.survey.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.butakov.survey.domain.User;
import ru.butakov.survey.service.utils.ConsoleServiceUtils;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConditionalOnProperty(value = "app.consoleService.enabled", havingValue = "true")
@RequiredArgsConstructor
public class ConsoleServiceCommandlineImpl implements CommandLineRunner, ConsoleService {
    final ConsoleServiceUtils consoleServiceUtils;
    final IOService ioService;
    User user;

    @Override
    public void startTest() {
        if (isLoggedUser())
            ioService.printString(consoleServiceUtils.getTestResultString(user));
    }

    @Override
    public void print() {
        ioService.printString(consoleServiceUtils.testToString());
    }

    @Override
    public void login(String username) {
        user = consoleServiceUtils.getUser(username);
        ioService.printString("Logged as " + user.getUsername());
    }

    @Override
    public void run(String... args) {
        print();
        ioService.printString("Write your username please");
        login(ioService.readString());
        startTest();
    }

    boolean isLoggedUser() {
        return user != null;
    }
}
