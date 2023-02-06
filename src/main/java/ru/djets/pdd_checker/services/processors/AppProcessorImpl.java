package ru.djets.pdd_checker.services.processors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.rest.dto.TicketDto;

import java.util.Map;

@Component
@Setter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppProcessorImpl implements AppProcessor {

    private final Map<Long, TicketDto> chatIdTicketSelectedMap;
    private final Map<Long, QuestionDto> chatIdQuestionsSelectedMap;

    @Override
    public boolean isCorrectAnswer(int numberSelectedAnswer, QuestionDto questionDto) {
        boolean correctAnswer = false;
        for (int i = 1; i <= questionDto.getAnswerDtoList().size(); i++) {
            if (numberSelectedAnswer == i &&
                    questionDto.getAnswerDtoList().get(i - 1).getCorrectAnswer()) {
                correctAnswer = true;
                break;
            }
        }
        return correctAnswer;
    }

    @Override
    public Map<Long, TicketDto> getChatIdTicketSelectedMap() {
        return this.chatIdTicketSelectedMap;
    }

    @Override
    public Map<Long, QuestionDto> getChatIdQuestionsSelectedMap() {
        return this.chatIdQuestionsSelectedMap;
    }
}
