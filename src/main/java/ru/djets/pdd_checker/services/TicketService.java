package ru.djets.pdd_checker.services;

import ru.djets.pdd_checker.domain.Ticket;
import ru.djets.pdd_checker.rest.dto.TicketDto;

import java.util.List;

public interface TicketService {
    void save(Ticket ticket);
    TicketDto getById(long id);
    List<TicketDto> getAll();
    TicketDto getByTicketNumber(int ticketNumber);
}
