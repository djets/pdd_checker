package ru.djets.pdd_checker.rest.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

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
}
