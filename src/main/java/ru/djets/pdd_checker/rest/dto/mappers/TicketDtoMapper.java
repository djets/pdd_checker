package ru.djets.pdd_checker.rest.dto.mappers;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.djets.pdd_checker.domain.Question;
import ru.djets.pdd_checker.domain.Ticket;
import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.rest.dto.TicketDto;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TicketDtoMapper {

    AnswerDtoMapper answerDtoMapper;

    public TicketDto toDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(ticket.getId());
        ticketDto.setNumberTicketDto(ticket.getTicketNumber());
        if (ticket.getQuestions() != null && !ticket.getQuestions().isEmpty()) {
            for (Question question : ticket.getQuestions()) {
                QuestionDto questionDto = new QuestionDto();
                questionDto.setId(question.getId());
                questionDto.setTextQuestion(question.getTextQuestion());
                questionDto.setPathImage(question.getPathImage());
                questionDto.setDescription(question.getDescription());
                Optional.ofNullable(question.getAnswers())
                        .orElseGet(Collections::emptyList).stream()
                        .map(answerDtoMapper::toDto)
                        .forEach(questionDto::addAnswerDto);
                ticketDto.addQuestionDto(questionDto);

            }
        }
        return ticketDto;
    }

    public Ticket fromDto(@NotNull TicketDto ticketDto) {
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(ticketDto.getNumberTicketDto());
        if (ticketDto.getQuestionDtoList() != null && !ticketDto.getQuestionDtoList().isEmpty()) {
            for (QuestionDto questionDto : ticketDto.getQuestionDtoList()) {
                Question question = new Question();
                question.setId(question.getId());
                question.setTextQuestion(question.getTextQuestion());
                question.setPathImage(question.getPathImage());
                question.setDescription(question.getDescription());
                Optional.ofNullable(questionDto.getAnswerDtoList())
                        .orElseGet(Collections::emptyList).stream()
                        .map(answerDtoMapper::fromDto)
                        .forEach(question::addAnswer);
                ticketDto.addQuestionDto(questionDto);

            }
        }
        return ticket;
    }
}

