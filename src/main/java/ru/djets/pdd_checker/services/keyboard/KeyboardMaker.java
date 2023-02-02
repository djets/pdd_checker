package ru.djets.pdd_checker.services.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.djets.pdd_checker.enums.CallbackPrefix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KeyboardMaker {

    public static InlineKeyboardMarkup getInlineKeyboardWithSequenceNumbers(CallbackPrefix callbackPrefix, List<?> objects) {
        InlineKeyboardMarkup ticketsInlineKeyboard = new InlineKeyboardMarkup();
        ticketsInlineKeyboard.setKeyboard(new ArrayList<>());
        if(callbackPrefix.equals(CallbackPrefix.ANSWER_)) {
            ticketsInlineKeyboard.getKeyboard()
                    .add(List.of(InlineKeyboardButton.builder()
                            .callbackData(CallbackPrefix.DESCRIPTION_.toString())
                            .text("show description")
                            .build()));
        }
        ticketsInlineKeyboard.getKeyboard()
                .add(getListNumberOfObjects(objects)
                .stream()
                .map(i -> InlineKeyboardButton.builder()
                        .callbackData(callbackPrefix.toString() + i)
                        .text(String.valueOf(i)).build())
                .collect(Collectors.toList()));
        return ticketsInlineKeyboard;
    }

    public static ReplyKeyboardMarkup getMainReplyKeyboard() {
        KeyboardRow rowOne = new KeyboardRow(2);
        rowOne.add(KeyboardButton.builder().text("stop").build());
        rowOne.add(KeyboardButton.builder().text("next question").build());

        KeyboardRow rowTwo = new KeyboardRow(2);
        rowTwo.add(KeyboardButton.builder().text("back to questions").build());
        rowTwo.add(KeyboardButton.builder().text("ticket selection").build());

        return ReplyKeyboardMarkup.builder()
                .clearKeyboard()
                .keyboard(List.of(rowOne, rowTwo))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

    private static List<Integer> getListNumberOfObjects(List<?> objects) {
        AtomicInteger numberOfObject = new AtomicInteger(1);
        return objects.stream()
                .map(question -> numberOfObject.getAndIncrement())
                .collect(Collectors.toList());
    }
}
