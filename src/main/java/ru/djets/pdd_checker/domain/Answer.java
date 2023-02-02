package ru.djets.pdd_checker.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

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
    public String toString() {
        return answerText;
    }
}
