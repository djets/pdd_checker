package ru.djets.pdd_checker.rest.dto.mappers;

import org.springframework.stereotype.Component;
import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.domain.Question;
import ru.djets.pdd_checker.rest.dto.AnswerDto;
import ru.djets.pdd_checker.rest.dto.QuestionDto;

@Component
public class QuestionDtoMapper {
    AnswerDtoMapper answerDtoMapper;

    public QuestionDto toDto(Question question) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(question.getId())
                .setTextQuestion(question.getTextQuestion())
                .setDescription(question.getDescription())
                .setPathImage(question.getPathImage());
        if (question.getAnswers() != null && !question.getAnswers().isEmpty()) {
            for (Answer answer : question.getAnswers()) {
                AnswerDto answerDto = new AnswerDto();
                answerDto.setId(answer.getId());
                answerDto.setCorrectAnswer(answer.getCorrectAnswer());
                answerDto.setAnswerText(answer.getAnswerText());
                questionDto.addAnswerDto(answerDto);
            }
//            question.getAnswers().stream()
//                    .map(answerDtoMapper::toDto)
//                    .forEach(questionDto::addAnswerDto);
        }
        return questionDto;
    }

    public Question fromDto(QuestionDto questionDto) {
        Question question = new Question();
        question.setId(questionDto.getId());
        question.setTextQuestion(questionDto.getTextQuestion());
        question.setDescription(questionDto.getDescription());
        if (questionDto.getAnswerDtoList() != null && !questionDto.getAnswerDtoList().isEmpty()) {
            questionDto.getAnswerDtoList().stream()
                    .map(answerDtoMapper::fromDto)
                    .forEach(question::addAnswer);
        }
        return question;


    }
}
