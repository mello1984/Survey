package ru.butakov.survey.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.butakov.survey.domain.User;
import ru.butakov.survey.service.utils.ConsoleServiceUtils;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ConsoleServiceCommandLineImplTest {
    @Mock
    private ConsoleServiceUtils utils;
    @Mock
    private IOService ioService;

    @Test
    void startTest() {
        ConsoleServiceCommandlineImpl commandline = Mockito.spy(new ConsoleServiceCommandlineImpl(utils,ioService));
        String expected = "test string";
        Mockito.when(commandline.isLoggedUser()).thenReturn(true);
        Mockito.when(utils.getTestResultString(Mockito.isNull())).thenReturn(expected);

        commandline.startTest();
        Mockito.verify(ioService).printString(expected);
        Mockito.verifyNoMoreInteractions(ioService);
        Mockito.verify(utils).getTestResultString(Mockito.isNull());
        Mockito.verifyNoMoreInteractions(utils);
    }

    @Test
    void print() {
        ConsoleServiceCommandlineImpl commandline = new ConsoleServiceCommandlineImpl(utils, ioService);
        String expected = "test string";
        Mockito.when(utils.testToString()).thenReturn(expected);

        commandline.print();
        Mockito.verify(ioService).printString(expected);
        Mockito.verifyNoMoreInteractions(ioService);
        Mockito.verify(utils).testToString();
        Mockito.verifyNoMoreInteractions(utils);
    }

    @Test
    void login() throws Exception {
        ConsoleServiceCommandlineImpl commandline = new ConsoleServiceCommandlineImpl(utils, ioService);
        String username = "username";
        User user = User.builder().username(username).build();
        Mockito.when(utils.getUser(username)).thenReturn(user);

        commandline.login(username);
        Mockito.verify(ioService).printString("Logged as " + username );
        Mockito.verifyNoMoreInteractions(ioService);

        Field field = commandline.getClass().getDeclaredField("user");
        field.setAccessible(true);
        User aUser = (User) field.get(commandline);
        assertThat(aUser).isEqualTo(user);
        Mockito.verify(utils).getUser(username);
        Mockito.verifyNoMoreInteractions(utils);
    }

    @Test
    void isLoggedUser_returnUnavailable_whenUserIsNull() {
        ConsoleServiceCommandlineImpl commandline = new ConsoleServiceCommandlineImpl(utils,ioService);
        assertThat(commandline.isLoggedUser()).isFalse();
    }

    @Test
    void isLoggedUser_returnAvailable_whenUserExists() throws Exception {
        ConsoleServiceCommandlineImpl commandline = new ConsoleServiceCommandlineImpl(utils,ioService);

        Field field = commandline.getClass().getDeclaredField("user");
        field.setAccessible(true);
        field.set(commandline, User.builder().build());

        assertThat(commandline.isLoggedUser()).isTrue();
    }
}