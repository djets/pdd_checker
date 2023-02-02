package ru.djets.pdd_checker.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.domain.Question;
import ru.djets.pdd_checker.domain.Ticket;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.yml")
@DataJpaTest
public class AnswersJpaRepositoryTest {
    @Autowired
    AnswerJpaRepository repository;

    @Test
    void getId() {
        Ticket exeptionTicket = new Ticket();
        exeptionTicket.setId(1);
        exeptionTicket.setTicketNumber(1);

        Question exeptionQuestion = new Question();
        exeptionQuestion.setId(1);
        exeptionQuestion.setTextQuestion("Question1");

        Answer expectedAnswer = new Answer();
        expectedAnswer.setAnswerText("Answer1");
        expectedAnswer.setCorrectAnswer(false);

        exeptionQuestion.addAnswer(expectedAnswer);
        exeptionTicket.addQuestion(exeptionQuestion);

        repository.save(expectedAnswer);

        assertThat(repository.findById(1L).orElse(null)).usingRecursiveComparison().isEqualTo(expectedAnswer);

    }
}
