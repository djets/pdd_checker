package ru.djets.pdd_checker.services.processors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.djets.pdd_checker.enums.CallbackPrefix;
import ru.djets.pdd_checker.rest.controllers.PddCheckLongPollingBotController;
import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.services.QuestionService;
import ru.djets.pdd_checker.services.TicketService;
import ru.djets.pdd_checker.services.handlers.CallbackQueryHandler;
import ru.djets.pdd_checker.services.handlers.MessageHandler;

import java.util.*;

import static ru.djets.pdd_checker.services.keyboard.KeyboardMaker.getInlineKeyboardNextQuestion;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TelegramBotWebhookUpdateProcessorImpl implements TelegramBotWebhookUpdateProcessor {
    Logger logger = LoggerFactory.getLogger(TelegramBotWebhookUpdateProcessorImpl.class);

    TicketService ticketService;
    QuestionService questionService;
    CallbackQueryHandler callbackQueryHandler;
    MessageHandler messageHandler;
    PddCheckLongPollingBotController pddCheckLongPollingBotController;

    Map<Long, Integer> chatIdSelectedTicketMap = new HashMap<>();
    Map<Long, Long> chatIdSelectedQuestionMap = new HashMap<>();

    @Override
    public BotApiMethod<?> processUpdate(Update update) {
        SendMessage requestMessage;
        if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            logger.info("ChatId: " + chatId);
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            logger.info("MessageId: " + messageId);
            String data = update.getCallbackQuery().getData();

            int numberTicket;
            long questionId;

            if (data.startsWith(CallbackPrefix.TICKET_.toString())) {
                numberTicket = Integer.parseInt(data.replace(CallbackPrefix.TICKET_.toString(), ""));

                chatIdSelectedTicketMap.merge(chatId, numberTicket, (k, v) -> numberTicket);

                requestMessage = callbackQueryHandler.getMessageWithInlineKeyboardForAllTicketQuestions(
                        questionService.getAllByTicketNumber(numberTicket).size(), chatId.toString());
                return requestMessage;

            } else if (data.startsWith(CallbackPrefix.QUESTION_.toString())) {
                questionId = Long.parseLong(data.replace(CallbackPrefix.QUESTION_.toString(), ""));
                chatIdSelectedQuestionMap.merge(chatId, questionId, (k, v) -> questionId);
                QuestionDto questionDto = questionService.getById(questionId);

                if(questionDto.getPathImage() != null) {
                    pddCheckLongPollingBotController.executeSendPhoto(
                            callbackQueryHandler.getSendPhotoWithQuestionAndInlineKeyboardForAnswers(
                            questionDto, chatId.toString()));
                } else {
                    requestMessage = callbackQueryHandler.getMessageWithQuestionAndInlineKeyboardForAnswers(
                            questionService.getById(questionId), chatId.toString());
                    return requestMessage;
                }

            } else if (data.startsWith(CallbackPrefix.ANSWER_.toString())) {
                int numberSelectedAnswer = Integer.parseInt(data.replace(CallbackPrefix.ANSWER_.toString(), ""));
                if (chatIdSelectedQuestionMap.get(chatId) != null) {
                    QuestionDto questionDto = questionService.getById(chatIdSelectedQuestionMap.get(chatId));
                    boolean correctAnswer = false;
                    for(int i = 1; i <= questionDto.getAnswerDtoList().size(); i++) {
                        if (numberSelectedAnswer == i && questionDto.getAnswerDtoList().get(i - 1).getCorrectAnswer()) {
                            correctAnswer = true;
                            break;
                        };
                    }
                    if(update.getCallbackQuery().getMessage().hasPhoto()) {
                        EditMessageMedia editMessageMedia = new EditMessageMedia();
                        String caption = update.getCallbackQuery().getMessage().getCaption();
                        InlineKeyboardMarkup messageKeyboard = update.getCallbackQuery().getMessage().getReplyMarkup();
                        List<PhotoSize> photos = update.getCallbackQuery().getMessage().getPhoto();
                        String fileId = photos.stream()
                                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                                .findFirst()
                                .orElse(null).getFileId();
                        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto(fileId);

                        inputMediaPhoto.setCaption(caption + "\n\n" + questionDto.getDescription());

                        editMessageMedia.setChatId(chatId.toString());
                        editMessageMedia.setMessageId(messageId);
                        editMessageMedia.setMedia(inputMediaPhoto);
                        editMessageMedia.setReplyMarkup(getInlineKeyboardNextQuestion(messageKeyboard, numberSelectedAnswer, correctAnswer));
                        pddCheckLongPollingBotController.executeEditMedia(editMessageMedia);
                    } else {
                        InlineKeyboardMarkup messageKeyboard = update.getCallbackQuery().getMessage().getReplyMarkup();
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(chatId.toString());
                        editMessageText.setMessageId(messageId);
                        editMessageText.setText(update.getCallbackQuery().getMessage().getText() +
                                "\n\n" + questionDto.getDescription());
                        editMessageText.setReplyMarkup(getInlineKeyboardNextQuestion(messageKeyboard, numberSelectedAnswer, correctAnswer));
                        return editMessageText;
                    }
                } else {
                    return SendMessage.builder().text("для начала выберите вопрос").chatId(chatId.toString()).build();
                }
            } else if (data.startsWith(CallbackPrefix.NEXT_.toString())) {
                Integer numberSelectedTicket = chatIdSelectedTicketMap.get(chatId);
                List<QuestionDto> questionDtoList = ticketService
                        .getByTicketNumber(numberSelectedTicket)
                        .getQuestionDtoList();
                QuestionDto questionDto = questionService.getById(chatIdSelectedQuestionMap.get(chatId));
                int numberNextQuestion = questionDtoList.indexOf(questionDto) + 1;
                if (numberNextQuestion < questionDtoList.size()) {
                    QuestionDto nextQuestionDto = questionDtoList.get(numberNextQuestion);
                    if(nextQuestionDto != null) {
                        if(nextQuestionDto.getPathImage() != null) {
                            pddCheckLongPollingBotController
                                    .executeSendPhoto(callbackQueryHandler
                                            .getSendPhotoWithQuestionAndInlineKeyboardForAnswers(
                                                    nextQuestionDto, chatId.toString()));
                        } else {
                            requestMessage = callbackQueryHandler
                                    .getMessageWithQuestionAndInlineKeyboardForAnswers(
                                            nextQuestionDto, chatId.toString());
                            return requestMessage;
                        }
                        chatIdSelectedQuestionMap.merge(chatId, nextQuestionDto.getId(),
                                (k, v) -> nextQuestionDto.getId());
                    }
                } else {
                    requestMessage = SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("В этом билете закончились вопросы. Перейдите к выбору билета нажав 'выбор билета'")
                            .build();
                    return requestMessage;
                }
            }

        } else if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            logger.info("ChatId: " + chatId);
            String messageText = update.getMessage().getText();
            if (messageText == null) {
                throw new RuntimeException("empty message text");
            } else if (messageText.equals("/start")) {
                requestMessage = messageHandler.getStartMessage(chatId.toString());
                return requestMessage;
            } else if (messageText.equals("вернутся к вопросам")) {
                if (chatIdSelectedTicketMap.containsKey(chatId)) {
                    return messageHandler.getQuestionSelection(
                            chatIdSelectedTicketMap.get(chatId),
                            chatId.toString());
                } else {
                    return SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Вы еще не выбрали билет. " +
                            "Нажмите кнопку выбора билета на встроенной клавиатуре")
                            .build();
                }
            } else if (messageText.equals("выбор билета")) {
                return messageHandler.getTicketSelection(chatId.toString());
            }
        }
        return null;
    }
}
