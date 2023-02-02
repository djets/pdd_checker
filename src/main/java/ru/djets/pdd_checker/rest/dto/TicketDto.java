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
public class TicketDto {
    long id;
    int numberTicketDto;
    List<QuestionDto> questionDtoList = new ArrayList<>();

    public void addQuestionDto(QuestionDto questionDto) {
        questionDtoList.add(questionDto);
        questionDto.setTicketDto(this);
    }

    public void removeQuestionDto(QuestionDto questionDto) {
        questionDtoList.remove(questionDto);
        questionDto.setTicketDto(null);
    }
}
