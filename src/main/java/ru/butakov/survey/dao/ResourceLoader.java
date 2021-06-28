package ru.butakov.survey.dao;

import java.io.InputStream;

public interface ResourceLoader {
    InputStream getInputStream(String filename);
}
