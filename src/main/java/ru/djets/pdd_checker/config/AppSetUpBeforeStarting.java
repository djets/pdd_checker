package ru.djets.pdd_checker.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.djets.pdd_checker.rest.controllers.PddCheckWebhookBotController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppSetUpBeforeStarting {
    Logger logger = LoggerFactory.getLogger(PddCheckWebhookBotController.class);
    WebClient webClient;
    TelegramBotProperties properties;
    String botUsername;
    String botToken;
    String apiUrl;
    String botPath;

    @Autowired
    public AppSetUpBeforeStarting(TelegramBotProperties properties, WebClient webClient) {
        this.properties = properties;
        this.webClient = webClient;
        this.botUsername = properties.getBotUsername();
        this.botPath = properties.getBotPath();
        this.apiUrl = getApiUrl();
        try {
            File file = new File(properties.getBotTokenPath());
            if(file.exists() && file.canRead()) {
                this.botToken = Files.readString(file.toPath(), StandardCharsets.US_ASCII).trim();
            }
        } catch (IOException e) {
            throw new RuntimeException("the token file was not found");
        }

        logger.info("===> BotPath: " + getBotPath());
        //For development
        this.webClient = webClient;
        String uri = ("/bot" + this.getBotToken() +  "/setWebhook?url=" + this.getBotPath()).replace("%0A", "");
        String requestSetUrl = webClient.post()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        logger.info("===> Set url: " + requestSetUrl);
    }
}
