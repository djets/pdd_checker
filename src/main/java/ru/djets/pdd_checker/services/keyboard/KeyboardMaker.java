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
            int numberOfButtons,
            int maxSizeRow
    ) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(new ArrayList<>());
        getListNumberOfSize(numberOfButtons, maxSizeRow)
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
        keyboardButtons
                .forEach(inlineKeyboardButton -> {
                    int numberButton = Integer.parseInt(inlineKeyboardButton.getText());
                    if (numberButton == numberSelectedAnswer) {
                        if (correctAnswer) {
                            inlineKeyboardButton.setText(EmojiParser.parseToUnicode(":white_check_mark:") + " " + numberButton);
                        } else {
                            inlineKeyboardButton.setText(EmojiParser.parseToUnicode(":x:") + " " + numberButton);
                        }
                    } else {
                        inlineKeyboardButton.setText(String.valueOf(numberButton));
                    }
                });
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

    private static List<List<Integer>> getListNumberOfSize(int numberOfButtons, int maxSizeRow) {
        List<List<Integer>> rowList = new ArrayList<>();
        if ((double) numberOfButtons / maxSizeRow >= 1) {
            for (int i = 1; i <= numberOfButtons / maxSizeRow; i++) {
                if (i == 1) {
                    rowList.add(Stream.iterate(1, n -> n + 1)
                            .limit(maxSizeRow)
                            .collect(Collectors.toList()));
                } else {
                    rowList.add(Stream.iterate((i - 1) * maxSizeRow + 1, n -> n + 1)
                            .limit(maxSizeRow)
                            .collect(Collectors.toList()));
                }
            }
            rowList.add(Stream.iterate((numberOfButtons / maxSizeRow) * maxSizeRow + 1, n -> n + 1)
                    .limit(numberOfButtons % maxSizeRow)
                    .collect(Collectors.toList()));
        } else {
            rowList.add(Stream.iterate(1, n -> n + 1)
                    .limit(numberOfButtons % maxSizeRow)
                    .collect(Collectors.toList()));
        }
        return rowList;
    }
}
