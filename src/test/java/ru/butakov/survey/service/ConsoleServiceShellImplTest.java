package ru.butakov.survey.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.shell.Availability;
import ru.butakov.survey.domain.User;
import ru.butakov.survey.service.utils.ConsoleServiceUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ConsoleServiceShellImplTest {
    @Mock
    private ConsoleServiceUtils utils;
    @Mock
    private IOService ioService;
    private final PrintStream systemOut = System.out;

    @Test
    void print() {
        ConsoleServiceShellImpl consoleServiceShell = new ConsoleServiceShellImpl(utils, ioService);
        String expected = "test string";
        Mockito.when(utils.testToString()).thenReturn(expected);

        consoleServiceShell.print();
        Mockito.verify(ioService).printString(expected);
        Mockito.verifyNoMoreInteractions(ioService);
        Mockito.verify(utils).testToString();
        Mockito.verifyNoMoreInteractions(utils);
    }

    @Test
    void login() throws Exception {
        String username = "username";
        User user = User.builder().username(username).build();
        ConsoleServiceShellImpl consoleServiceShell = new ConsoleServiceShellImpl(utils, ioService);
        Mockito.when(utils.getUser(username)).thenReturn(user);

        consoleServiceShell.login(username);
        Mockito.verify(ioService).printString("Logged as " + username );
        Mockito.verifyNoMoreInteractions(ioService);

        Field field = consoleServiceShell.getClass().getDeclaredField("user");
        field.setAccessible(true);
        User aUser = (User) field.get(consoleServiceShell);
        assertThat(aUser).isEqualTo(user);
        Mockito.verify(utils).getUser(username);
        Mockito.verifyNoMoreInteractions(utils);
    }

    @Test
    void startTest() {
        ConsoleServiceShellImpl consoleServiceShell = new ConsoleServiceShellImpl(utils, ioService);
        String expected = "test string";
        Mockito.when(utils.getTestResultString(Mockito.isNull())).thenReturn(expected);

        consoleServiceShell.startTest();
        Mockito.verify(ioService).printString(expected);
        Mockito.verifyNoMoreInteractions(ioService);
        Mockito.verify(utils).getTestResultString(Mockito.isNull());
        Mockito.verifyNoMoreInteractions(utils);
    }

    @Test
    void isLoggedUser_returnUnavailable_whenUserIsNull() {
        ConsoleServiceShellImpl consoleServiceShell = new ConsoleServiceShellImpl(utils, ioService);
        Availability expected = Availability.unavailable("Login please");
        Availability actual = consoleServiceShell.isLoggedUser();
        assertThat(actual.isAvailable()).isEqualTo(expected.isAvailable());
        assertThat(actual.getReason()).isEqualTo(expected.getReason());
    }

    @Test
    void isLoggedUser_returnAvailable_whenUserExists() throws Exception {
        ConsoleServiceShellImpl consoleServiceShell = new ConsoleServiceShellImpl(utils, ioService);

        Field field = consoleServiceShell.getClass().getDeclaredField("user");
        field.setAccessible(true);
        field.set(consoleServiceShell, User.builder().build());

        Availability expected = Availability.available();
        Availability actual = consoleServiceShell.isLoggedUser();
        assertThat(actual.isAvailable()).isEqualTo(expected.isAvailable());
    }
}