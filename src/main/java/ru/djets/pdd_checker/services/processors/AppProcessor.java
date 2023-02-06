package ru.djets.pdd_checker.services.processors;

import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.rest.dto.TicketDto;

import java.util.Map;

public interface AppProcessor {
    boolean isCorrectAnswer(int numberSelectedAnswer, QuestionDto questionDto);

    Map<Long, TicketDto> getChatIdTicketSelectedMap();

    Map<Long, QuestionDto> getChatIdQuestionsSelectedMap();
}
