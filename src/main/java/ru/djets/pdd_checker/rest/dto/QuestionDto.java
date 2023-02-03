package ru.djets.pdd_checker.rest.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionDto {
    long id;
    String textQuestion;
    List<AnswerDto> answerDtoList = new ArrayList<>();
    String pathImage, description;
    TicketDto ticketDto;

    public void addAnswerDto(AnswerDto answerDto) {
        answerDtoList.add(answerDto);
        answerDto.setQuestionDto(this);
    }

    public void removeAnswerDto(AnswerDto answerDto) {
        answerDtoList.remove(answerDto);
        answerDto.setQuestionDto(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionDto that = (QuestionDto) o;

        if (!textQuestion.equals(that.textQuestion)) return false;
        if (!Objects.equals(pathImage, that.pathImage)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = textQuestion.hashCode();
        result = 31 * result + (pathImage != null ? pathImage.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
