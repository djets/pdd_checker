package ru.djets.pdd_checker.rest.controllers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface WebhookRestController {
    BotApiMethod<?> onUpdateReceived(Update update);
}
