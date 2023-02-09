package ru.djets.pdd_checker.rest.dto.mappers;

import org.springframework.stereotype.Component;
import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.domain.Question;
import ru.djets.pdd_checker.rest.dto.AnswerDto;
import ru.djets.pdd_checker.rest.dto.QuestionDto;

import java.util.List;

@Component
public class QuestionDtoMapperImpl implements QuestionDtoMapper {

    @Override
    public QuestionDto toDto(Question question) {
        QuestionDto questionDto = new QuestionDto()
                .setId(question.getId())
                .setTextQuestion(question.getTextQuestion())
                .setDescription(question.getDescription())
                .setPathImage(question.getPathImage());
        List<Answer> answers = question.getAnswers();
        if (answers != null && !answers.isEmpty()) {
            for (Answer answer : answers) {
                AnswerDto answerDto = new AnswerDto()
                        .setId(answer.getId())
                        .setCorrectAnswer(answer.getCorrectAnswer())
                        .setAnswerText(answer.getAnswerText());
                questionDto.addAnswerDto(answerDto);
            }
        }
        return questionDto;
    }

    @Override
    public Question fromDto(QuestionDto questionDto) {
        Question question = new Question()
                .setId(questionDto.getId())
                .setTextQuestion(questionDto.getTextQuestion())
                .setDescription(questionDto.getDescription());
        List<AnswerDto> answerDtoList = questionDto.getAnswerDtoList();
        if (answerDtoList != null && !answerDtoList.isEmpty()) {
            for (AnswerDto answerDto : answerDtoList) {
                Answer answer = new Answer()
                        .setId(answerDto.getId())
                        .setCorrectAnswer(answerDto.getCorrectAnswer())
                        .setAnswerText(answerDto.getAnswerText());
                question.addAnswer(answer);
            }
        }
        return question;


    }
}
