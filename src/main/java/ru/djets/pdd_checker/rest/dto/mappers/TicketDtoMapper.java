package ru.djets.pdd_checker.rest.dto.mappers;

import ru.djets.pdd_checker.domain.Ticket;
import ru.djets.pdd_checker.rest.dto.TicketDto;

public interface TicketDtoMapper {
    TicketDto toDto(Ticket ticket);

    Ticket fromDto(TicketDto ticketDto);
}
