package ru.djets.pdd_checker.services.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageHandler {
    SendMessage getStartMessage(String chatId);
    SendMessage getEndMessage(String chatId);
    SendMessage getTicketSelection(String chatId);

    SendMessage getQuestionSelection(int ticketNumber, String chatId);
}
