package ru.djets.pdd_checker.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Entity
@Table(name = "answers")
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;
    @Column(name = "answer_text", nullable = false)
    String answerText;
    @Column(name = "correct_answer", nullable = false)
    Boolean correctAnswer;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "question_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_answer_question_id"))
    Question question;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (!answerText.equals(answer.answerText)) return false;
        return Objects.equals(correctAnswer, answer.correctAnswer);
    }

    @Override
    public int hashCode() {
        int result = answerText.hashCode();
        result = 31 * result + (correctAnswer != null ? correctAnswer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return answerText;
    }
}
