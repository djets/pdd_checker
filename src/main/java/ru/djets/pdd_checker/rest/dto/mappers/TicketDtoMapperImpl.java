package ru.djets.pdd_checker.rest.dto.mappers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.djets.pdd_checker.domain.Question;
import ru.djets.pdd_checker.domain.Ticket;
import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.rest.dto.TicketDto;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TicketDtoMapperImpl implements TicketDtoMapper {
    QuestionDtoMapper questionDtoMapper;

    @Override
    public TicketDto toDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto()
                .setId(ticket.getId())
                .setNumberTicketDto(ticket.getTicketNumber());
        List<Question> questions = ticket.getQuestions();
        if (questions != null && !questions.isEmpty()) {
            questions.stream()
                    .map(questionDtoMapper::toDto)
                    .forEach(ticketDto::addQuestionDto);
        }
        return ticketDto;
    }

    @Override
    public Ticket fromDto(TicketDto ticketDto) {
        Ticket ticket = new Ticket()
                .setTicketNumber(ticketDto.getNumberTicketDto());
        List<QuestionDto> questionDtoList = ticketDto.getQuestionDtoList();
        if (questionDtoList != null && !questionDtoList.isEmpty()) {
            questionDtoList.stream()
                    .map(questionDtoMapper::fromDto)
                    .forEach(ticket::addQuestion);
        }
        return ticket;
    }
}

