package ru.djets.pdd_checker.services.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import ru.djets.pdd_checker.rest.dto.QuestionDto;

import java.util.List;

public interface CallbackQueryHandler {

    SendMessage getMessageWithInlineKeyboardForAllTicketQuestions(int size, String chatId);

    SendMessage getMessageWithQuestionAndInlineKeyboardForAnswers(QuestionDto questionDto, String chatId);

    SendPhoto getSendPhotoWithQuestionAndInlineKeyboardForAnswers(QuestionDto questionDto, String chatId);
}
