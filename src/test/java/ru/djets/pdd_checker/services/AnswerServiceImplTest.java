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
import ru.djets.pdd_checker.repositories.AnswerJpaRepository;
import ru.djets.pdd_checker.rest.dto.AnswerDto;
import ru.djets.pdd_checker.rest.dto.mappers.AnswerDtoMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        expectedAnswer = new Answer()
                .setId(1L)
                .setAnswerText("Answer1")
                .setCorrectAnswer(false)
                .setQuestion(null);

        expectedAnswerDto = new AnswerDto()
                .setId(1L)
                .setAnswerText("Answer1")
                .setCorrectAnswer(false)
                .setQuestionDto(null);

    }

    @Test
    void saveTest() {
        Mockito.when(repository.save(expectedAnswer)).thenReturn(expectedAnswer);
        Mockito.when(answerDtoMapper.toDto(expectedAnswer)).thenReturn(expectedAnswerDto);
        assertThat(answerService.save(expectedAnswer)).usingRecursiveComparison().isEqualTo(expectedAnswerDto);
    }

    @Test
    void getByIdTest() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(expectedAnswer));
        Mockito.when(answerDtoMapper.toDto(expectedAnswer)).thenReturn(expectedAnswerDto);
        assertThat(answerService.getById(1L)).usingRecursiveComparison().isEqualTo(expectedAnswerDto);
    }

    @Test
    void getAllTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of(expectedAnswer));
        Mockito.when(answerDtoMapper.toDto(expectedAnswer)).thenReturn(expectedAnswerDto);
        assertThat(answerService.getAll().size()).isEqualTo(1);
        assertThat(answerService.getAll().get(0)).usingRecursiveComparison().isEqualTo(expectedAnswerDto);
    }
}