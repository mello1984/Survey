package ru.butakov.survey.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IOServiceConsoleImpl implements IOService {
    PrintStream out;
    Scanner sc;

    public IOServiceConsoleImpl(@Value("#{ T (java.lang.System).out}") PrintStream out,
                                @Value("#{ T (java.lang.System).in}") InputStream in) {
        this.out = out;
        this.sc = new Scanner(in);
    }

    @Override
    public void printString(String text) {
        out.println(text);
    }

    @Override
    public String readString() {
        return sc.nextLine();
    }
}
