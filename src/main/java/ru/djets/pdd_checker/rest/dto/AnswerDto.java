package ru.djets.pdd_checker.rest.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerDto {
    long id;
    String answerText;
    Boolean correctAnswer;
    QuestionDto questionDto;
}
