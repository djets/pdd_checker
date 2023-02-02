package ru.djets.pdd_checker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.djets.pdd_checker.domain.Question;

import java.util.List;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByTicket_TicketNumber(int ticketNumber);
}