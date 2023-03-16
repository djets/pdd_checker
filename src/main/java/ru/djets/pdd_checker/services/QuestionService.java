package ru.djets.pdd_checker.services;

import org.springframework.transaction.annotation.Transactional;
import ru.djets.pdd_checker.domain.Question;
import ru.djets.pdd_checker.rest.dto.QuestionDto;

import java.util.List;
public interface QuestionService {
    QuestionDto save(Question question);
    QuestionDto getById(long id);
    List<QuestionDto> getAll();
    List<QuestionDto> getAllByTicketNumber(int ticket);

    @Transactional(readOnly = true)
    int getCountAllByTicketNumber(int ticketNumber);
}
