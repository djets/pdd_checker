package ru.djets.pdd_checker.rest.controllers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.djets.pdd_checker.config.AppSetUpBeforeStarting;
import ru.djets.pdd_checker.config.TelegramBotProperties;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component
@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PddCheckLongPollingBotController extends TelegramLongPollingBot {
    AppSetUpBeforeStarting setUpBeforeStarting;

    @Autowired
    public PddCheckLongPollingBotController(AppSetUpBeforeStarting setUpBeforeStarting) {
        this.setUpBeforeStarting = setUpBeforeStarting;
    }

    @Override
    public String getBotUsername() {
        return setUpBeforeStarting.getBotPath();
    }

    @Override
    public String getBotToken() {
        return setUpBeforeStarting.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    public void executeSendPhoto(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    public void executeSendSticker(SendSticker sendSticker) {
        try {
            execute(sendSticker);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    public void executeEditMedia(EditMessageMedia editMessageMedia) {
        try {
            execute(editMessageMedia);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }
}
