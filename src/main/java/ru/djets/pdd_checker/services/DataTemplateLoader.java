package ru.djets.pdd_checker.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.domain.Question;
import ru.djets.pdd_checker.domain.Ticket;

import java.util.Random;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DataTemplateLoader {
    TicketService ticketService;

    public void createQuestion(int count) {
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(1);
        Stream.iterate(1, n -> n + 1)
                .limit(count)
                .map(integer -> {
                    Question question = new Question();
                    question.setTextQuestion("Вопрос: " + integer + "Разрешен ли Вам съезд на дорогу с грунтовым покрытием?");
                    question.setDescription("Description: " + integer);
                    question.setPathImage("images/0102.jpg");
                    Stream.iterate(1, n -> n + 1)
                            .limit(3)
                            .map(integer1 -> {
                                Answer answer = new Answer();
                                answer.setAnswerText("Answer-" + integer1);
                                answer.setCorrectAnswer(new Random().nextBoolean());
                                return answer;
                            })
                            .forEach(question::addAnswer);
                    return question;
                })
                .forEach(ticket::addQuestion);
        ticketService.save(ticket);
    }
}
