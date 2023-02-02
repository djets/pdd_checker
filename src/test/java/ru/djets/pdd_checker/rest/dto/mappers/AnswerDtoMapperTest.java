package ru.djets.pdd_checker.rest.dto.mappers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.rest.dto.AnswerDto;

import static org.assertj.core.api.Assertions.assertThat;

@FieldDefaults(level = AccessLevel.PRIVATE)
class AnswerDtoMapperTest {
    AnswerDtoMapper answerDtoMapper;
    Answer expectedAnswer;
    AnswerDto expectedAnswerDto;

    public AnswerDtoMapperTest() {
        this.answerDtoMapper = new AnswerDtoMapper(new QuestionDtoMapper());
    }

    @BeforeEach
    void setUp() {
        expectedAnswer = new Answer();
        expectedAnswer.setId(1L);
        expectedAnswer.setAnswerText("Answer1");
        expectedAnswer.setCorrectAnswer(false);
        expectedAnswer.setQuestion(null);

        expectedAnswerDto = new AnswerDto();
        expectedAnswerDto.setId(1L);
        expectedAnswerDto.setAnswerText("Answer1");
        expectedAnswerDto.setCorrectAnswer(false);
        expectedAnswerDto.setQuestionDto(null);
    }

    @Test
    void toDto() {
        assertThat(answerDtoMapper.toDto(expectedAnswer)).usingRecursiveComparison().isEqualTo(expectedAnswerDto);
    }

    @Test
    void fromDto() {
        assertThat(answerDtoMapper.fromDto(expectedAnswerDto)).usingRecursiveComparison().isEqualTo(expectedAnswer);
    }
}