package ru.butakov.survey.dao;

import ru.butakov.survey.domain.Question;

import java.util.List;

public interface QuestionRepository {
    List<Question> findAll();

    Question findById(int id);
}
