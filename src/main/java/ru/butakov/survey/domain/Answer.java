package ru.butakov.survey.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Answer {
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Question question;
    String text;
    boolean right;
}
