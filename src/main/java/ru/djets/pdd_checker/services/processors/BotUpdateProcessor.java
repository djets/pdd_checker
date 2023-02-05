package ru.djets.pdd_checker.services.processors;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotUpdateProcessor {
    BotApiMethod<?> processUpdate(Update update);
}
