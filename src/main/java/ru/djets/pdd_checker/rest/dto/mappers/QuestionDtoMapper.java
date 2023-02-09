package ru.djets.pdd_checker.rest.dto.mappers;

import ru.djets.pdd_checker.domain.Question;
import ru.djets.pdd_checker.rest.dto.QuestionDto;

public interface QuestionDtoMapper {
    QuestionDto toDto(Question question);

    Question fromDto(QuestionDto questionDto);
}
