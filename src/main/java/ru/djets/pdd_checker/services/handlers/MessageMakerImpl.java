package ru.djets.pdd_checker.services.handlers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.djets.pdd_checker.enums.CallbackPrefix;
import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.services.keyboard.KeyboardMaker;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.djets.pdd_checker.services.keyboard.KeyboardMaker.getInlineKeyboardNextQuestion;


@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageMakerImpl implements MessageMaker {

    @Override
    public SendMessage getMessageWithInlineKeyboardForAllTicketQuestions(
            String chatId,
            int numberOfButtons
    ) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Выберите вопрос \n")
                .replyMarkup(
                        KeyboardMaker.getInlineKeyboardWithSequenceNumbers(
                                CallbackPrefix.QUESTION_,
                                numberOfButtons, 5)
                )
                .build();
    }

    @Override
    public SendMessage getMessageWithQuestionAndInlineKeyboardForAnswers(
            QuestionDto questionDto,
            String chatId
    ) {
        AtomicInteger numberOfObject = new AtomicInteger(1);
        return SendMessage.builder()
                .chatId(chatId)
                .text(questionDto.getTextQuestion() + "\n" +
                        questionDto.getAnswerDtoList().stream()
                                .map(answer -> numberOfObject.getAndIncrement() + ". "
                                        + answer.getAnswerText())
                                .collect(Collectors.joining("\n")))
                .replyMarkup(KeyboardMaker.getInlineKeyboardWithSequenceNumbers(
                        CallbackPrefix.ANSWER_,
                        questionDto.getAnswerDtoList().size(),
                        5))
                .build();
    }

    @Override
    public SendPhoto getSendPhotoWithQuestionAndInlineKeyboardForAnswers(
            QuestionDto questionDto,
            String chatId
    ) {
        AtomicInteger numberOfObject = new AtomicInteger(1);
        SendPhoto sendPhoto = new SendPhoto();
        ClassPathResource pathResource = new ClassPathResource(
                questionDto.getPathImage(),
                ClassLoader.getSystemClassLoader()
        );
        try {
            File image = pathResource.getFile();
            sendPhoto.setPhoto(new InputFile(image));
        } catch (IOException e) {
            throw new RuntimeException("File image not found " + e.getMessage());
        }
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(
                questionDto.getTextQuestion() + "\n" +
                        questionDto.getAnswerDtoList().stream()
                                .map(answer -> numberOfObject.getAndIncrement() +
                                        ". " + answer.getAnswerText())
                                .collect(Collectors.joining("\n")
                                )
        );
        sendPhoto.setReplyMarkup(
                KeyboardMaker.getInlineKeyboardWithSequenceNumbers(
                        CallbackPrefix.ANSWER_,
                        questionDto.getAnswerDtoList().size(),
                        5)
        );
        return sendPhoto;
    }

    @Override
    public SendMessage getStartMessage(String chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Добро пожаловать в бот проверки знаний ПДД! \n" +
                        "Данное приложение создано и запускается ИСКЛЮЧИТЕЛЬНО \nв ознакомительных целях.\n" +
                        "Данные взяты из открытых источников.\n" +
                        "Для начала работы приложения отправьте команду /start")
                .replyMarkup(KeyboardMaker.getMainReplyKeyboard())
                .build();
    }

    @Override
    public SendMessage getTickets(
            String chatId,
            int listTicketsSize
    ) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Выберите билет. \n")
                .replyMarkup(KeyboardMaker
                        .getInlineKeyboardWithSequenceNumbers(
                                CallbackPrefix.TICKET_,
                                listTicketsSize,
                                8))
                .build();
    }

    @Override
    public SendMessage getQuestionSelectionTicket(int ticketNumber, String chatId, int listQuestionsSize) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Вопросы билета " + ticketNumber + ". \n")
                .replyMarkup(KeyboardMaker
                        .getInlineKeyboardWithSequenceNumbers(
                                CallbackPrefix.QUESTION_,
                                listQuestionsSize,
                                5))
                .build();
    }

    @Override
    public EditMessageText getEditMessageText(
            Update update,
            int numberSelectedAnswer,
            String description,
            boolean correctAnswer
    ) {
        EditMessageText editMessageText = new EditMessageText();
        InlineKeyboardMarkup messageKeyboard = update.getCallbackQuery().getMessage().getReplyMarkup();
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText(update.getCallbackQuery().getMessage().getText() +
                "\n\n" + description);
        editMessageText.setReplyMarkup(
                getInlineKeyboardNextQuestion(
                        messageKeyboard, numberSelectedAnswer, correctAnswer));
        return editMessageText;
    }

    @Override
    public EditMessageMedia getEditMessageMedia(
            Update update,
            int numberSelectedAnswer,
            String description,
            boolean correctAnswer
    ) {
        EditMessageMedia editMessageMedia = new EditMessageMedia();
        String caption = update.getCallbackQuery().getMessage().getCaption();
        InlineKeyboardMarkup messageKeyboard = update.getCallbackQuery().getMessage().getReplyMarkup();
        List<PhotoSize> photos = update.getCallbackQuery().getMessage().getPhoto();
        String fileId = photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null).getFileId();
        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto(fileId);

        inputMediaPhoto.setCaption(caption + "\n\n" + description);

        editMessageMedia.setChatId(
                update.getCallbackQuery().getMessage().getChatId().toString()
        );
        editMessageMedia.setMessageId(
                update.getCallbackQuery().getMessage().getMessageId()
        );
        editMessageMedia.setMedia(inputMediaPhoto);
        editMessageMedia.setReplyMarkup(
                getInlineKeyboardNextQuestion(
                        messageKeyboard, numberSelectedAnswer, correctAnswer));
        return editMessageMedia;
    }

    @Override
    public SendMessage getMessageWrongSelectedTicket(long chatId) {
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Вы еще не выбрали билет. " +
                        "Нажмите кнопку 'выбора билета' на встроенной клавиатуре")
                .build();
    }

    @Override
    public SendMessage getMessageWrongSelectedQuestion(long chatId) {
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Вы еще не выбрали вопрос. " +
                        "Нажмите кнопку 'вернутся к вопросам' на встроенной клавиатуре")
                .build();
    }

    @Override
    public SendMessage getMessageOutOfTickets(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("В этом билете закончились вопросы. " +
                        "Перейдите к выбору билета нажав 'выбор билета'")
                .build();
    }
}


