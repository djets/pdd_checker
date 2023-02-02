package ru.djets.pdd_checker.rest.controllers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.djets.pdd_checker.config.AppSetUpBeforeStarting;
import ru.djets.pdd_checker.config.TelegramBotProperties;
import ru.djets.pdd_checker.services.processors.TelegramBotWebhookUpdateProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component
@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PddCheckWebhookBotController extends TelegramWebhookBot {
    Logger logger = LoggerFactory.getLogger(PddCheckWebhookBotController.class);

    AppSetUpBeforeStarting setUpBeforeStarting;
    TelegramBotWebhookUpdateProcessor botService;

    @Autowired
    public PddCheckWebhookBotController(
            AppSetUpBeforeStarting setUpBeforeStarting,
            TelegramBotWebhookUpdateProcessor telegramBotWebhookUpdateProcessor) {
        this.setUpBeforeStarting = setUpBeforeStarting;
        this.botService = telegramBotWebhookUpdateProcessor;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update !=null) {
            return botService.processUpdate(update);
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return setUpBeforeStarting.getBotPath();
    }

    @Override
    public String getBotUsername() {
        return setUpBeforeStarting.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return setUpBeforeStarting.getBotToken();
    }
}


