-- CREATE SCHEMA public;
CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1;
CREATE TABLE tickets
(
    id            BIGINT  NOT NULL,
    ticket_number INTEGER NOT NULL,
    CONSTRAINT pk_tickets PRIMARY KEY (id)
);

CREATE TABLE questions
(
    id            BIGINT NOT NULL,
    text_question VARCHAR(3000),
    path_image    VARCHAR(3000),
    description   VARCHAR(3000),
    ticket_id     BIGINT,
    CONSTRAINT pk_questions PRIMARY KEY (id)
);

ALTER TABLE questions
    ADD CONSTRAINT FK_QUESTIONS_ON_TICKET FOREIGN KEY (ticket_id) REFERENCES tickets (id);

CREATE TABLE answers
(
    id             BIGINT NOT NULL,
    answer_text    VARCHAR(255),
    correct_answer BOOLEAN,
    question_id    BIGINT,
    CONSTRAINT pk_answers PRIMARY KEY (id)
);

ALTER TABLE answers
    ADD CONSTRAINT FK_ANSWERS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES questions (id);