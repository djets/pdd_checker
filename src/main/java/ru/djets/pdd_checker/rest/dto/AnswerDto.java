package ru.djets.pdd_checker.rest.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerDto {
    long id;
    String answerText;
    Boolean correctAnswer;
    QuestionDto questionDto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerDto answerDto = (AnswerDto) o;

        if (!answerText.equals(answerDto.answerText)) return false;
        return correctAnswer.equals(answerDto.correctAnswer);
    }

    @Override
    public int hashCode() {
        int result = answerText.hashCode();
        result = 31 * result + correctAnswer.hashCode();
        return result;
    }
}
