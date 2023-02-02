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
import ru.djets.pdd_checker.config.TelegramBotProperties;

@Component
@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PddCheckLongPollingBotController extends TelegramLongPollingBot {
    TelegramBotProperties properties;

    @Autowired
    public PddCheckLongPollingBotController(TelegramBotProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getBotUsername() {
        return properties.getBotPath();
    }

    @Override
    public String getBotToken() {
        return properties.getBotToken();
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
