package ru.butakov.survey.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.butakov.survey.aop.Loggable;
import ru.butakov.survey.domain.User;
import ru.butakov.survey.service.utils.ConsoleServiceUtils;

@ShellComponent
@Loggable
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ConsoleServiceShellImpl implements ConsoleService {
    final ConsoleServiceUtils consoleServiceUtils;
    User user;

    @Override
    @ShellMethod(value = "Print questions command", key = {"p", "print"})
    public void print() {
        System.out.println(consoleServiceUtils.testToString());
    }

    @Override
    @ShellMethod(value = "Login method", key = {"l", "login"})
    public void login(String username) {
        user = consoleServiceUtils.getUser(username);
        System.out.println("Logged as " + user.getUsername());
    }

    @Override
    @ShellMethod(value = "Start testing", key = {"t", "test"})
    @ShellMethodAvailability(value = "isLoggedUser")
    public void startTest() {
        String result = consoleServiceUtils.getTestResultString(user);
        System.out.println(result);
    }

    Availability isLoggedUser() {
        return user != null ? Availability.available() : Availability.unavailable("Login please");
    }
}
