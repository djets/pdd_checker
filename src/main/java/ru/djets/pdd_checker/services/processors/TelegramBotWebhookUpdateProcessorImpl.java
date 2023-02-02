package ru.djets.pdd_checker.services.processors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.djets.pdd_checker.enums.CallbackPrefix;
import ru.djets.pdd_checker.rest.controllers.PddCheckLongPollingBotController;
import ru.djets.pdd_checker.rest.dto.AnswerDto;
import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.services.QuestionService;
import ru.djets.pdd_checker.services.handlers.CallbackQueryHandler;
import ru.djets.pdd_checker.services.handlers.MessageHandler;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TelegramBotWebhookUpdateProcessorImpl implements TelegramBotWebhookUpdateProcessor {
    Logger logger = LoggerFactory.getLogger(TelegramBotWebhookUpdateProcessorImpl.class);
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
                        questionService.getAllByTicketNumber(numberTicket), chatId.toString());
                return requestMessage;

            } else if (data.startsWith(CallbackPrefix.QUESTION_.toString())) {
                questionId = Long.parseLong(data.replace(CallbackPrefix.QUESTION_.toString(), ""));
                chatIdSelectedQuestionMap.merge(chatId, questionId, (k, v) -> questionId);
                QuestionDto questionDto = questionService.getById(questionId);

                if(questionDto.getPathImage() != null) {
                    pddCheckLongPollingBotController.executeSendPhoto(callbackQueryHandler.getSendPhotoWithQuestionAndInlineKeyboardForAnswers(
                            questionService.getById(questionId), chatId.toString()));
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
                    if (correctAnswer) {
                       return SendMessage.builder().text("верно").chatId(chatId.toString()).build();
                    }

                    if(update.getCallbackQuery().getMessage().hasPhoto()) {
                        EditMessageMedia editMessageMedia = new EditMessageMedia();
                        String caption = update.getCallbackQuery().getMessage().getCaption();
                        List<PhotoSize> photos = update.getCallbackQuery().getMessage().getPhoto();
                        String fileId = photos.stream()
                                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                                .findFirst()
                                .orElse(null).getFileId();
                        InlineKeyboardMarkup replyMarkup = editMessageMedia.getReplyMarkup();
                        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto(fileId);
                        inputMediaPhoto.setCaption(caption + "\n<============>\n" + questionDto.getDescription());

                        editMessageMedia.setChatId(chatId.toString());
                        editMessageMedia.setMessageId(messageId);
                        editMessageMedia.setMedia(inputMediaPhoto);
                        editMessageMedia.setReplyMarkup(replyMarkup);
                        pddCheckLongPollingBotController.executeEditMedia(editMessageMedia);
                    } else {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(chatId.toString());
                        editMessageText.setMessageId(messageId);
                        editMessageText.setText(questionDto.getTextQuestion() + "\n<============>\n" + questionDto.getDescription());
                        //TODO добавить inlineKeyboard 'next question'
                        return editMessageText;
                    }
                } else {
                    return SendMessage.builder().text("для начала выберите вопрос").chatId(chatId.toString()).build();
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
            } else if (messageText.equals("show description")) {

                //TODO
                return null;
            } else if (messageText.equals("next question")) {
                //TODO
                return null;
            } else if (messageText.equals("back to questions")) {
                if (chatIdSelectedTicketMap.containsKey(chatId)) {
                    return messageHandler.getQuestionSelection(chatIdSelectedTicketMap.get(chatId), chatId.toString());
                } else {
                    return SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Вы еще не выбрали билет. " +
                            "Нажмите кнопку выбора билета на встроенной клавиатуре")
                            .build();
                }
            } else if (messageText.equals("back to tickets")) {
                return messageHandler.getTicketSelection(chatId.toString());
            }
        }
        return null;
    }
}
