package ru.butakov.survey.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class IOServiceConsoleImplTest {

    @Test
    void printString() throws IOException {
        String expected = "test string";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(expected.getBytes());
        IOService ioService = new IOServiceConsoleImpl(new PrintStream(baos), bais);

        ioService.printString(expected);
        baos.flush();
        assertThat(baos.toString()).isEqualTo(expected + System.lineSeparator());
    }

    @Test
    void readString() {
        String expected = "test string";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(expected.getBytes());
        IOService ioService = new IOServiceConsoleImpl(new PrintStream(baos), bais);

        String actual = ioService.readString();
        assertThat(actual).isEqualTo(expected);
    }
}