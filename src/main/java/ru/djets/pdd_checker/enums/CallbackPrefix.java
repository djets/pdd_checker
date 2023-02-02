package ru.djets.pdd_checker.enums;

import lombok.Getter;
import ru.djets.pdd_checker.domain.Answer;

@Getter
public enum CallbackPrefix {
    TICKET_,
    QUESTION_,
    ANSWER_,
    DESCRIPTION_;
}
