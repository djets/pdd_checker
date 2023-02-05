package ru.djets.pdd_checker.services.keyboard;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.djets.pdd_checker.enums.CallbackPrefix;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeyboardMaker {

    public static InlineKeyboardMarkup getInlineKeyboardWithSequenceNumbers(
            CallbackPrefix callbackPrefix,
            int size
    ) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(new ArrayList<>());
        getListNumberOfSize(size)
                .forEach(listRow -> inlineKeyboardMarkup.getKeyboard()
                        .add(listRow
                                .stream()
                                .map(i -> InlineKeyboardButton.builder()
                                        .callbackData(callbackPrefix.toString() + i)
                                        .text(String.valueOf(i)).build())
                                .collect(Collectors.toList())));
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getInlineKeyboardNextQuestion(
            InlineKeyboardMarkup messageKeyboard,
            int numberSelectedAnswer,
            boolean correctAnswer
    ) {
        List<InlineKeyboardButton> keyboardButtons = messageKeyboard.getKeyboard().get(0);
        keyboardButtons.stream()
                .map(inlineKeyboardButton -> {
                    int numberButton = Integer.parseInt(inlineKeyboardButton.getText());
                    if (numberButton == numberSelectedAnswer) {
                        if (correctAnswer) {
                            inlineKeyboardButton.setText(EmojiParser.parseToUnicode(":white_check_mark:") + " " + numberButton);
                        }
                        inlineKeyboardButton.setText(EmojiParser.parseToUnicode(":x:") + " " + numberButton);
                    } else {
                        inlineKeyboardButton.setText(String.valueOf(numberButton));
                    }
                    return inlineKeyboardButton;
                })
                .collect(Collectors.toList());
        messageKeyboard.getKeyboard()
                .add(List.of(InlineKeyboardButton.builder()
                        .callbackData(CallbackPrefix.NEXT_.toString())
                        .text("Следующий вопрос билета").build()));
        return messageKeyboard;
    }

    public static ReplyKeyboardMarkup getMainReplyKeyboard() {
        KeyboardRow row = new KeyboardRow(2);
        row.add(KeyboardButton.builder().text("вернутся к вопросам").build());
        row.add(KeyboardButton.builder().text("выбор билета").build());

        return ReplyKeyboardMarkup.builder()
                .clearKeyboard()
                .keyboard(List.of(row))
                .selective(true)
                .resizeKeyboard(true)
                .build();
    }

    private static List<List<Integer>> getListNumberOfSize(int size) {
        List<List<Integer>> rowList = new ArrayList<>();
        if ((double) size / 8 >= 1) {
            for (int i = 1; i <= size / 8; i++) {
                if (i == 1) {
                    rowList.add(Stream.iterate(1, n -> n + 1)
                            .limit(8)
                            .collect(Collectors.toList()));
                } else {
                    rowList.add(Stream.iterate((i - 1) * 8 + 1, n -> n + 1)
                            .limit(8)
                            .collect(Collectors.toList()));
                }
            }
            rowList.add(Stream.iterate((size / 8) * 8 + 1, n -> n + 1)
                    .limit(size % 8)
                    .collect(Collectors.toList()));
        } else {
            rowList.add(Stream.iterate(1, n -> n + 1)
                    .limit(size % 8)
                    .collect(Collectors.toList()));
        }
        return rowList;

    }
}
