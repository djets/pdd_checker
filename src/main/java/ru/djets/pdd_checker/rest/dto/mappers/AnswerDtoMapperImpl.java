package ru.djets.pdd_checker.rest.dto.mappers;

import org.springframework.stereotype.Component;
import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.rest.dto.AnswerDto;

@Component
public class AnswerDtoMapperImpl implements AnswerDtoMapper{

    @Override
    public AnswerDto toDto(Answer answer) {
        return new AnswerDto()
                .setId(answer.getId())
                .setCorrectAnswer(answer.getCorrectAnswer())
                .setAnswerText(answer.getAnswerText());
    }

    @Override
    public Answer fromDto(AnswerDto answerDto) {
        return new Answer()
                .setId(answerDto.getId())
                .setAnswerText(answerDto.getAnswerText())
                .setCorrectAnswer(answerDto.getCorrectAnswer());
    }
}
