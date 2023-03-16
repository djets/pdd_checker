package ru.djets.pdd_checker.rest.dto.mappers;

import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.rest.dto.AnswerDto;

public interface AnswerDtoMapper {

    AnswerDto toDto(Answer answer);

    Answer fromDto(AnswerDto answerDto);
}
