package ru.butakov.survey.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Question {
    int id;
    QuestionType type;
    String text;
    int points;
    List<Answer> answers;

    public Question(int id, QuestionType type, String text, int points) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.points = points;
        answers = new ArrayList<>();
    }

    public void addAnswer(Answer answer){
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }
}
