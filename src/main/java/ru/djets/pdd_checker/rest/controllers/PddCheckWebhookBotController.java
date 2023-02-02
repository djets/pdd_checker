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
import ru.djets.pdd_checker.config.TelegramBotProperties;
import ru.djets.pdd_checker.services.processors.TelegramBotWebhookUpdateProcessor;

@Component
@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PddCheckWebhookBotController extends TelegramWebhookBot {
    Logger logger = LoggerFactory.getLogger(PddCheckWebhookBotController.class);
    TelegramBotProperties properties;
    TelegramBotWebhookUpdateProcessor botService;

    WebClient webClient;

    @Autowired
    public PddCheckWebhookBotController(
            TelegramBotProperties properties,
            TelegramBotWebhookUpdateProcessor telegramBotWebhookUpdateProcessor, WebClient webClient) {
        this.properties = properties;
        this.botService = telegramBotWebhookUpdateProcessor;
        logger.info("===> BotPath: " + properties.getBotPath());

        //For development
        this.webClient = webClient;

        String requestSetUrl = webClient.post()
                .uri("/bot" + getBotToken() +  "/setWebhook?url=" + getBotPath())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        logger.info("===> Set url: " + requestSetUrl);
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
        return properties.getBotPath();
    }

    @Override
    public String getBotUsername() {
        return properties.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return properties.getBotToken();
    }
}


