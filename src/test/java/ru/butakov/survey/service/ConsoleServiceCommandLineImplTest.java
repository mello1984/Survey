package ru.butakov.survey.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.butakov.survey.domain.User;
import ru.butakov.survey.service.utils.ConsoleServiceUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ConsoleServiceCommandLineImplTest {
    @Mock
    private ConsoleServiceUtils utils;
    private final PrintStream systemOut = System.out;

    @Test
    void print() throws IOException {
        ConsoleServiceCommandlineImpl commandline = new ConsoleServiceCommandlineImpl(utils);
        String expected = "test string";
        Mockito.when(utils.testToString()).thenReturn(expected);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        commandline.print();
        baos.flush();

        String actual = baos.toString();
        assertThat(actual).isEqualTo(expected + System.lineSeparator());
        System.setOut(systemOut);
        Mockito.verify(utils).testToString();
        Mockito.verifyNoMoreInteractions(utils);
    }

    @Test
    void login() throws Exception {
        String username = "username";
        User user = User.builder().username(username).build();
        ConsoleServiceCommandlineImpl commandline = new ConsoleServiceCommandlineImpl(utils);
        Mockito.when(utils.getUser(username)).thenReturn(user);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));


        commandline.login(username);
        baos.flush();

        String actual = baos.toString();
        assertThat(actual).isEqualTo("Logged as " + username + System.lineSeparator());
        System.setOut(systemOut);

        Field field = commandline.getClass().getDeclaredField("user");
        field.setAccessible(true);
        User aUser = (User) field.get(commandline);
        assertThat(aUser).isEqualTo(user);
        Mockito.verify(utils).getUser(username);
        Mockito.verifyNoMoreInteractions(utils);
    }

    @Test
    void startTest() throws Exception {
        ConsoleServiceCommandlineImpl commandline = Mockito.spy(new ConsoleServiceCommandlineImpl(utils));
        String expected = "test string";
        Mockito.when(commandline.isLoggedUser()).thenReturn(true);
        Mockito.when(utils.getTestResultString(Mockito.isNull())).thenReturn(expected);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        commandline.startTest();
        System.setOut(systemOut);
        baos.flush();

        String actual = baos.toString();
        assertThat(actual).isEqualTo(expected + System.lineSeparator());
        Mockito.verify(utils).getTestResultString(Mockito.isNull());
        Mockito.verifyNoMoreInteractions(utils);
    }

    @Test
    void isLoggedUser_returnUnavailable_whenUserIsNull() {
        ConsoleServiceCommandlineImpl commandline = new ConsoleServiceCommandlineImpl(utils);
        assertThat(commandline.isLoggedUser()).isFalse();
    }

    @Test
    void isLoggedUser_returnAvailable_whenUserExists() throws Exception {
        ConsoleServiceCommandlineImpl commandline = new ConsoleServiceCommandlineImpl(utils);

        Field field = commandline.getClass().getDeclaredField("user");
        field.setAccessible(true);
        field.set(commandline, User.builder().build());

        assertThat(commandline.isLoggedUser()).isTrue();
    }
}