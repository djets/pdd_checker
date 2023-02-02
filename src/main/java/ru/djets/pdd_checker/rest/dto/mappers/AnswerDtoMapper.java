package ru.djets.pdd_checker.rest.dto.mappers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.rest.dto.AnswerDto;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AnswerDtoMapper {
    QuestionDtoMapper questionDtoMapper;

    public AnswerDto toDto(Answer answer) {
        AnswerDto answerDto = new AnswerDto();
        answerDto.setId(answer.getId());
        answerDto.setCorrectAnswer(answer.getCorrectAnswer());
        answerDto.setAnswerText(answer.getAnswerText());
        return answerDto;
    }

    public Answer fromDto(AnswerDto answerDto) {
        Answer answer = new Answer();
        answer.setId(answerDto.getId());
        answer.setAnswerText(answerDto.getAnswerText());
        answer.setCorrectAnswer(answerDto.getCorrectAnswer());
        return answer;
    }
}
