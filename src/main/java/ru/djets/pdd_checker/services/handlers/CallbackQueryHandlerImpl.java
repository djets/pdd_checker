package ru.djets.pdd_checker.services.handlers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.djets.pdd_checker.enums.CallbackPrefix;
import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.services.keyboard.KeyboardMaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CallbackQueryHandlerImpl implements CallbackQueryHandler {

    @Override
    public SendMessage getMessageWithInlineKeyboardForAllTicketQuestions(int size, String chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Вопросы: ")
                .replyMarkup(KeyboardMaker.getInlineKeyboardWithSequenceNumbers(CallbackPrefix.QUESTION_, size))
//                .text("Вопросы билета: " + questionsDto.get(0).getTicketDto().getNumberTicketDto() + "\n")
                .build();
    }

    @Override
    public SendMessage getMessageWithQuestionAndInlineKeyboardForAnswers(QuestionDto questionDto, String chatId) {
        AtomicInteger numberOfObject = new AtomicInteger(1);
        return SendMessage.builder()
                .chatId(chatId)
                .text(questionDto.getTextQuestion() + "\n" +
                        questionDto.getAnswerDtoList().stream()
                                .map(answer -> numberOfObject.getAndIncrement() + ". " + answer.getAnswerText())
                                .collect(Collectors.joining("\n")))
                .replyMarkup(KeyboardMaker.getInlineKeyboardWithSequenceNumbers(
                        CallbackPrefix.ANSWER_,
                        questionDto.getAnswerDtoList().size()))
                .build();
    }

    @Override
    public SendPhoto getSendPhotoWithQuestionAndInlineKeyboardForAnswers(QuestionDto questionDto, String chatId) {
        AtomicInteger numberOfObject = new AtomicInteger(1);
        SendPhoto sendPhoto = new SendPhoto();
        ClassPathResource pathResource = new ClassPathResource(questionDto.getPathImage(), ClassLoader.getSystemClassLoader());
        Path pathFile = Paths.get(pathResource.getPath());
            try {
                File image = pathResource.getFile();
                sendPhoto.setPhoto(new InputFile(image));
            } catch (IOException e) {
                throw new RuntimeException("File image not found " + e.getMessage());
            }
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(questionDto.getTextQuestion() + "\n" +
                questionDto.getAnswerDtoList().stream()
                        .map(answer -> numberOfObject.getAndIncrement() + ". " + answer.getAnswerText())
                        .collect(Collectors.joining("\n")));
        sendPhoto.setReplyMarkup(KeyboardMaker
                .getInlineKeyboardWithSequenceNumbers(
                        CallbackPrefix.ANSWER_,
                        questionDto.getAnswerDtoList().size()));
        return sendPhoto;
    }
}


