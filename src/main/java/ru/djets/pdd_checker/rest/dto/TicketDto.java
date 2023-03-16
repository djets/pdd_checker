package ru.djets.pdd_checker.rest.dto;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketDto ticketDto = (TicketDto) o;

        return numberTicketDto == ticketDto.numberTicketDto;
    }

    @Override
    public int hashCode() {
        return numberTicketDto;
    }
}
