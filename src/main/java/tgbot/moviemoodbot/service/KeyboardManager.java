package tgbot.moviemoodbot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class KeyboardManager {

    public void bindTestKeyboard(SendMessage message) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"a", "b", "c"});
        rows.add(new String[]{"d", "e"});
        bindKeyboard(message, createReplyKeyboardMarkup(rows));
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup(List<String[]> rows) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (String[] row : rows) {
            var keyBoardRow = new KeyboardRow();
            keyBoardRow.addAll(Arrays.asList(row));
            keyboardRows.add(keyBoardRow);
        }

        return new ReplyKeyboardMarkup(keyboardRows);
    }

    private void bindKeyboard(SendMessage message, ReplyKeyboardMarkup markup) {
        message.setReplyMarkup(markup);
    }
}
