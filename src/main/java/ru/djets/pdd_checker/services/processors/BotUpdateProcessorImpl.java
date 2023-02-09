package ru.djets.pdd_checker.services.processors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.djets.pdd_checker.enums.CallbackPrefix;
import ru.djets.pdd_checker.rest.controllers.PddCheckLongPollingBotController;
import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.rest.dto.TicketDto;
import ru.djets.pdd_checker.services.QuestionService;
import ru.djets.pdd_checker.services.TicketService;
import ru.djets.pdd_checker.services.handlers.MessageMaker;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BotUpdateProcessorImpl implements BotUpdateProcessor {
    Logger logger = LoggerFactory.getLogger(BotUpdateProcessorImpl.class);
    TicketService ticketService;
    QuestionService questionService;
    AppProcessor appProcessor;
    MessageMaker messageMaker;
    PddCheckLongPollingBotController pddCheckLongPollingBotController;

    @Override
    public BotApiMethod<?> processUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            logger.info("ChatId: " + chatId);
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            logger.info("MessageId: " + messageId);
            String data = update.getCallbackQuery().getData();

            if (data.startsWith(CallbackPrefix.TICKET_.toString())) {
                int numberSelectedTicket = Integer.parseInt(data.replace(CallbackPrefix.TICKET_.toString(), ""));
                TicketDto selectedTicketDto = ticketService.getByTicketNumber(numberSelectedTicket);
                appProcessor.getChatIdTicketSelectedMap().merge(
                        chatId,
                        selectedTicketDto, (k, v) -> selectedTicketDto
                );

                return messageMaker.getMessageWithInlineKeyboardForAllTicketQuestions(
                        chatId.toString(),
                        questionService.getAllByTicketNumber(numberSelectedTicket).size()
                );

            } else if (data.startsWith(CallbackPrefix.QUESTION_.toString())) {
                int selectedNumberQuestionDto = Integer.parseInt(
                        data.replace(CallbackPrefix.QUESTION_.toString(), "")
                );
                TicketDto selectedTicketDto = appProcessor.getChatIdTicketSelectedMap().get(chatId);
                if(selectedTicketDto != null) {
                    QuestionDto selectedQuestionDto = selectedTicketDto
                            .getQuestionDtoList()
                            .get(selectedNumberQuestionDto - 1);
                    appProcessor.getChatIdQuestionsSelectedMap().merge(
                            chatId,
                            selectedQuestionDto,
                            (k, v) -> selectedQuestionDto
                    );
                    if (selectedQuestionDto.getPathImage() != null) {
                        pddCheckLongPollingBotController.executeSendPhoto(
                                messageMaker.getSendPhotoWithQuestionAndInlineKeyboardForAnswers(
                                        selectedQuestionDto, chatId.toString()));
                    } else {
                        return messageMaker.getMessageWithQuestionAndInlineKeyboardForAnswers(
                                selectedQuestionDto, chatId.toString());
                    }
                } else {
                    return messageMaker.getMessageWrongSelectedQuestion(chatId);
                }

            } else if (data.startsWith(CallbackPrefix.ANSWER_.toString())) {
                int numberSelectedAnswer = Integer.parseInt(data.replace(
                        CallbackPrefix.ANSWER_.toString(), "")
                );
                if (appProcessor.getChatIdQuestionsSelectedMap().containsKey(chatId)) {
                    QuestionDto selectedQuestionDto = appProcessor.getChatIdQuestionsSelectedMap().get(chatId);
                    boolean correctAnswer = appProcessor.isCorrectAnswer(numberSelectedAnswer, selectedQuestionDto);

                    if (update.getCallbackQuery().getMessage().hasPhoto()) {
                        pddCheckLongPollingBotController
                                .executeEditMedia(
                                        messageMaker.getEditMessageMedia(
                                                update,
                                                numberSelectedAnswer,
                                                selectedQuestionDto.getDescription(),
                                                correctAnswer)
                                );
                    } else {
                        return messageMaker.getEditMessageText(
                                update,
                                numberSelectedAnswer,
                                selectedQuestionDto.getDescription(),
                                correctAnswer
                        );
                    }
                } else {
                    return SendMessage.builder()
                            .text("для начала выберите вопрос")
                            .chatId(chatId.toString())
                            .build();
                }

            } else if (data.startsWith(CallbackPrefix.NEXT_.toString())) {
                TicketDto selectedTicketDto = appProcessor.getChatIdTicketSelectedMap().get(chatId);
                QuestionDto selectedQuestionDto = appProcessor.getChatIdQuestionsSelectedMap().get(chatId);
                List<QuestionDto> questionDtoList = selectedTicketDto.getQuestionDtoList();

                int numberNextQuestion = questionDtoList.indexOf(selectedQuestionDto) + 1;

                if (numberNextQuestion < questionDtoList.size()) {
                    QuestionDto nextQuestionDto = questionDtoList.get(numberNextQuestion);
                    if (nextQuestionDto != null) {
                        if (nextQuestionDto.getPathImage() != null) {
                            pddCheckLongPollingBotController
                                    .executeSendPhoto(messageMaker
                                            .getSendPhotoWithQuestionAndInlineKeyboardForAnswers(
                                                    nextQuestionDto,
                                                    chatId.toString()
                                            )
                                    );
                        } else {
                            return messageMaker.getMessageWithQuestionAndInlineKeyboardForAnswers(
                                            nextQuestionDto, chatId.toString()
                            );
                        }
                        appProcessor.getChatIdQuestionsSelectedMap()
                                .merge(chatId, nextQuestionDto, (k, v) -> nextQuestionDto);
                    }
                } else {
                    return messageMaker.getMessageOutOfTickets(chatId);
                }
            }

        } else if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            logger.info("ChatId: " + chatId);
            String messageText = update.getMessage().getText();

            if (messageText == null) {
                throw new RuntimeException("empty message text");
            } else if (messageText.equals("/start")) {
                return messageMaker.getStartMessage(chatId.toString());
            } else if (messageText.equals("вернутся к вопросам")) {
                if (appProcessor.getChatIdTicketSelectedMap().containsKey(chatId)) {
                    int numberSelectedTicketDto = appProcessor.getChatIdTicketSelectedMap()
                            .get(chatId).getNumberTicketDto();
                    return messageMaker.getQuestionSelectionTicket(
                            numberSelectedTicketDto,
                            chatId.toString(),
                            questionService.getCountAllByTicketNumber(numberSelectedTicketDto)
                    );
                } else {
                    return messageMaker.getMessageWrongSelectedTicket(chatId);
                }
            } else if (messageText.equals("выбор билета")) {
                return messageMaker.getTickets(chatId.toString(), ticketService.count());
            }
        }
        return null;
    }
}
