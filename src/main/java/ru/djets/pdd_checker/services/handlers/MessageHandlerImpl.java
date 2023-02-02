package ru.djets.pdd_checker.services.handlers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.djets.pdd_checker.enums.CallbackPrefix;
import ru.djets.pdd_checker.services.QuestionService;
import ru.djets.pdd_checker.services.TicketService;
import ru.djets.pdd_checker.services.keyboard.KeyboardMaker;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageHandlerImpl implements MessageHandler {
    TicketService ticketService;
    QuestionService questionService;

    @Override
    public SendMessage getStartMessage(String chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Добро пожаловать в бот проверки знаний ПДД! Для начала работы приложения отправьте команду /start")
                .replyMarkup(KeyboardMaker.getMainReplyKeyboard())
                .build();
    }

    @Override
    public SendMessage getEndMessage(String chatId) {
        return null;
    }

    @Override
    public SendMessage getTicketSelection(String chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Билеты: \n")
                .replyMarkup(KeyboardMaker
                        .getInlineKeyboardWithSequenceNumbers(CallbackPrefix.TICKET_, ticketService.getAll()))
                .build();
    }

    @Override
    public SendMessage getQuestionSelection(int ticketNumber, String chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Вопрос: " + ticketNumber)
                .replyMarkup(KeyboardMaker
                        .getInlineKeyboardWithSequenceNumbers(CallbackPrefix.QUESTION_,
                                questionService.getAllByTicketNumber(ticketNumber)))
                .build();
    }
}
