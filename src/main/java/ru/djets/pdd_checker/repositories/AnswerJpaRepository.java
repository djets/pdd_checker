package ru.djets.pdd_checker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.djets.pdd_checker.domain.Answer;

public interface AnswerJpaRepository extends JpaRepository<Answer, Long> {
}