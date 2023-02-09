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
import ru.djets.pdd_checker.domain.Ticket;
import ru.djets.pdd_checker.repositories.TicketJpaRepository;
import ru.djets.pdd_checker.rest.dto.TicketDto;
import ru.djets.pdd_checker.rest.dto.mappers.TicketDtoMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestPropertySource(locations = "classpath:application-test.yml")
@ExtendWith(MockitoExtension.class)
@Import(TicketServiceImpl.class)
class TicketServiceImplTest {
    @MockBean
    TicketJpaRepository repository;
    @MockBean
    TicketDtoMapper ticketDtoMapper;
    @Autowired
    TicketService ticketService;
    Ticket expectedTicket;
    TicketDto expectedTicketDto;

    @BeforeEach
    void setUp() {
        expectedTicket = new Ticket()
                .setId(1L)
                .setTicketNumber(1);

        expectedTicketDto = new TicketDto()
                .setId(1L)
                .setNumberTicketDto(1);

    }

    @Test
    void saveTest() {
        Mockito.when(repository.save(expectedTicket)).thenReturn(expectedTicket);
        ticketService.save(expectedTicket);
        Mockito.verify(repository, Mockito.times(1)).save(expectedTicket);
    }

    @Test
    void getByIdTest() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(expectedTicket));
        Mockito.when(ticketDtoMapper.toDto(expectedTicket)).thenReturn(expectedTicketDto);
        assertThat(ticketService.getById(1L)).usingRecursiveComparison().isEqualTo(expectedTicketDto);
    }

    @Test
    void getAllTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of(expectedTicket));
        Mockito.when(ticketDtoMapper.toDto(expectedTicket)).thenReturn(expectedTicketDto);
        assertThat(ticketService.getAll().size()).isEqualTo(1);
        assertThat(ticketService.getAll().get(0)).usingRecursiveComparison().isEqualTo(expectedTicketDto);
    }

    @Test
    void getByTicketNumberTest() {
        Mockito.when(repository.findByTicketNumber(1)).thenReturn(Optional.of(expectedTicket));
        Mockito.when(ticketDtoMapper.toDto(expectedTicket)).thenReturn(expectedTicketDto);
        assertThat(ticketService.getByTicketNumber(1)).usingRecursiveComparison().isEqualTo(expectedTicketDto);
    }

    @Test
    void countTest() {
        Mockito.when(repository.count()).thenReturn(1L);
        assertThat(ticketService.count()).isEqualTo(1);
    }
}