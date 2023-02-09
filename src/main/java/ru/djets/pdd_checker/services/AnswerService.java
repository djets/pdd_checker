package ru.djets.pdd_checker.services;

import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.rest.dto.AnswerDto;

import java.util.List;

public interface AnswerService {
    AnswerDto save(Answer answer);
    AnswerDto getById(Long id);
    List<AnswerDto> getAll();
}
