package ru.djets.pdd_checker.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tickets")
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;
    @Column(name = "ticket_number", nullable = false, unique = true)
    int ticketNumber;
    @OneToMany(mappedBy = "ticket",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    List<Question> questions = new ArrayList<>();

    public void addQuestion(Question question) {
        questions.add(question);
        question.setTicket(this);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setTicket(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket ticket)) return false;

        return ticketNumber == ticket.ticketNumber;
    }

    @Override
    public int hashCode() {
        return ticketNumber;
    }

    @Override
    public String toString() {
        return String.valueOf(ticketNumber);
    }
}
