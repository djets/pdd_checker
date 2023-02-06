package ru.djets.pdd_checker.services.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.djets.pdd_checker.rest.dto.QuestionDto;

public interface MessageMaker {

    SendMessage getMessageWithInlineKeyboardForAllTicketQuestions(int size, String chatId);

    SendMessage getMessageWithQuestionAndInlineKeyboardForAnswers(QuestionDto questionDto, String chatId);

    SendPhoto getSendPhotoWithQuestionAndInlineKeyboardForAnswers(QuestionDto questionDto, String chatId);
    SendMessage getStartMessage(String chatId);
    SendMessage getTickets(String chatId, int listTicketsSize);
    SendMessage getQuestionSelectionTicket(int ticketNumber, String chatId, int listQuestionsSize);

    EditMessageText getEditMessageText(
            Update update,
            int numberSelectedAnswer,
            String description,
            boolean correctAnswer
    );

    EditMessageMedia getEditMessageMedia(
            Update update,
            int numberSelectedAnswer,
            String description,
            boolean correctAnswer
    );

    BotApiMethod<?> getMessageWrongSelectedTicket(long chatId);

    BotApiMethod<?> getMessageWrongSelectedQuestion(long chatId);
}
