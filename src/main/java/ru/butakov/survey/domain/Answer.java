package ru.butakov.survey.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Answer {
    @ToString.Exclude
    Question question;
    String text;
    boolean right;
}
