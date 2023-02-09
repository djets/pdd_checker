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
import ru.djets.pdd_checker.domain.Question;
import ru.djets.pdd_checker.domain.Ticket;
import ru.djets.pdd_checker.repositories.QuestionJpaRepository;
import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.rest.dto.TicketDto;
import ru.djets.pdd_checker.rest.dto.mappers.QuestionDtoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestPropertySource(locations = "classpath:application-test.yml")
@ExtendWith(MockitoExtension.class)
@Import(QuestionServiceImpl.class)
class QuestionServiceImplTest {
    @MockBean
    QuestionJpaRepository repository;
    @MockBean
    QuestionDtoMapper questionDtoMapper;
    @Autowired
    QuestionService questionService;
    Question expectedQuestion;
    QuestionDto expectedQuestionDto;

    @BeforeEach
    void setUp() {
        Ticket ticket = new Ticket(1L, 1, new ArrayList<>());
        expectedQuestion = new Question()
                .setId(1L)
                .setTextQuestion("Question1")
                .setDescription("Description1")
                .setPathImage("/");
        ticket.addQuestion(expectedQuestion);

        TicketDto ticketDto = new TicketDto(1L, 1, new ArrayList<>());
        expectedQuestionDto = new QuestionDto()
                .setId(1L)
                .setTextQuestion("Question1")
                .setDescription("Description1")
                .setPathImage("/");
        ticketDto.addQuestionDto(expectedQuestionDto);
    }

    @Test
    void saveTest() {
        Mockito.when(repository.save(expectedQuestion)).thenReturn(expectedQuestion);
        Mockito.when(questionDtoMapper.toDto(expectedQuestion)).thenReturn(expectedQuestionDto);
        assertThat(questionService.save(expectedQuestion)).usingRecursiveComparison().isEqualTo(expectedQuestionDto);
    }

    @Test
    void getByIdTest() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(expectedQuestion));
        Mockito.when(questionDtoMapper.toDto(expectedQuestion)).thenReturn(expectedQuestionDto);
        assertThat(questionService.getById(1L)).usingRecursiveComparison().isEqualTo(expectedQuestionDto);
    }

    @Test
    void getAllTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of(expectedQuestion));
        Mockito.when(questionDtoMapper.toDto(expectedQuestion)).thenReturn(expectedQuestionDto);
        assertThat(questionService.getAll().size()).isEqualTo(1);
        assertThat(questionService.getAll().get(0)).usingRecursiveComparison().isEqualTo(expectedQuestionDto);
    }

    @Test
    void getAllByTicketNumberTest() {
        Mockito.when(repository.findAllByTicket_TicketNumber(1)).thenReturn(List.of(expectedQuestion));
        Mockito.when(questionDtoMapper.toDto(expectedQuestion)).thenReturn(expectedQuestionDto);
        assertThat(questionService.getAllByTicketNumber(1)).usingRecursiveComparison().isEqualTo(List.of(expectedQuestionDto));
    }

    @Test
    void getCountAllByTicketNumberTest() {
        Mockito.when(repository.countAllByTicket_TicketNumber(1)).thenReturn(1);
        Mockito.when(questionDtoMapper.toDto(expectedQuestion)).thenReturn(expectedQuestionDto);
        assertThat(questionService.getCountAllByTicketNumber(1)).isEqualTo(1);
    }
}