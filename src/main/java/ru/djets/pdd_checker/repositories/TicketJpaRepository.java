package ru.djets.pdd_checker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.djets.pdd_checker.domain.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketJpaRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByTicketNumber(int ticketNumber);
}