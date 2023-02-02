package ru.djets.pdd_checker.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.rest.dto.AnswerDto;
import ru.djets.pdd_checker.rest.dto.mappers.AnswerDtoMapper;
import ru.djets.pdd_checker.repositories.AnswerJpaRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestPropertySource(locations = "classpath:application-test.yml")
@ExtendWith(MockitoExtension.class)
@Import(AnswerServiceImpl.class)
class AnswerServiceImplTest {
    @MockBean
    AnswerJpaRepository repository;
    @MockBean
    AnswerDtoMapper answerDtoMapper;

    @Autowired
    AnswerService answerService;
    Answer expectedAnswer;
    AnswerDto expectedAnswerDto;

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
    void save() {
        Mockito.when(repository.save(any())).thenReturn(expectedAnswer);
        Mockito.when(answerDtoMapper.toDto(expectedAnswer)).thenReturn(expectedAnswerDto);
        assertThat(answerService.save(expectedAnswer)).usingRecursiveComparison().isEqualTo(expectedAnswerDto);
    }

    @Test
    void getById() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(expectedAnswer));
        Mockito.when(answerDtoMapper.toDto(expectedAnswer)).thenReturn(expectedAnswerDto);
        assertThat(answerService.getById(1L)).usingRecursiveComparison().isEqualTo(expectedAnswerDto);
    }

    @Test
    void getAll() {
    }
}