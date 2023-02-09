package ru.djets.pdd_checker.rest.dto.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.djets.pdd_checker.domain.Ticket;
import ru.djets.pdd_checker.rest.dto.TicketDto;

import static org.assertj.core.api.Assertions.assertThat;
class TicketDtoMapperTest {
    final TicketDtoMapper ticketDtoMapper;
    Ticket expectedTicket;
    TicketDto expectedTicketDto;

    public TicketDtoMapperTest() {
        this.ticketDtoMapper = new TicketDtoMapperImpl(new QuestionDtoMapperImpl());
    }

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
    void toDtoTest() {
        assertThat(ticketDtoMapper.toDto(expectedTicket)).usingRecursiveComparison().isEqualTo(expectedTicketDto);
    }
}