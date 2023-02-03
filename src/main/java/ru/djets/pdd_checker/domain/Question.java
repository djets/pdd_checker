package ru.djets.pdd_checker.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "questions")
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;
    @Column(name = "text_question", length = 3000, nullable = false)
    String textQuestion;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Answer> answers = new ArrayList<>();
    @Column(name = "path_image", length = 3000)
    String pathImage;

    @Column(name = "description", length = 3000)
    String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(
            name = "ticket_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_question_ticket_id"))
    Ticket ticket;

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setQuestion(this);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
        answer.setQuestion(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (!textQuestion.equals(question.textQuestion)) return false;
        if (!Objects.equals(pathImage, question.pathImage)) return false;
        return Objects.equals(description, question.description);
    }

    @Override
    public int hashCode() {
        int result = textQuestion.hashCode();
        result = 31 * result + (pathImage != null ? pathImage.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return textQuestion;
    }
}
