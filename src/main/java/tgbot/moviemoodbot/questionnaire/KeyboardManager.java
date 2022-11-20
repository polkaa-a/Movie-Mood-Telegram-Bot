package tgbot.moviemoodbot.questionnaire;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import tgbot.moviemoodbot.questionnaire.buttons.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static tgbot.moviemoodbot.questionnaire.buttons.enums.CountryButton.COUNTRY_NO;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.CountryButton.COUNTRY_YES;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.GenreButton.GENRE_NO;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.GenreButton.GENRE_YES;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.RatingButton.RATING_NO;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.RatingButton.RATING_YES;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.YearButton.YEAR_NO;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.YearButton.YEAR_YES;

@Component
@RequiredArgsConstructor
public class KeyboardManager {

    private final AnswerManager answerManager;

    private static InlineKeyboardMarkup createInlineKeyboardMarkupFromMap(List<Map<String, String>> rows) {
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        for (Map<String, String> map : rows) {
            List<InlineKeyboardButton> keyBoardRow = new ArrayList<>();
            for (String key : map.keySet()) {
                var keyboardButton = new InlineKeyboardButton(map.get(key));
                keyboardButton.setCallbackData(key);
                keyBoardRow.add(keyboardButton);
            }
            keyboardRows.add(keyBoardRow);
        }

        return new InlineKeyboardMarkup(keyboardRows);
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkupFromList(List<List<Button>> buttons) {
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        for (List<Button> list : buttons) {
            List<InlineKeyboardButton> keyBoardRow = new ArrayList<>();
            for (Button button : list) {
                var keyboardButton = new InlineKeyboardButton(button.getText());
                keyboardButton.setCallbackData(button.getId());
                keyBoardRow.add(keyboardButton);
            }
            keyboardRows.add(keyBoardRow);
        }

        return new InlineKeyboardMarkup(keyboardRows);
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

    private SendMessage bindInlineKeyboardFromList(SendMessage message, List<List<Button>> buttons) {
        message.setReplyMarkup(createInlineKeyboardMarkupFromList(buttons));
        return message;
    }

    private SendMessage bindInlineKeyboardFromMap(SendMessage message, List<Map<String, String>> rows) {
        message.setReplyMarkup(createInlineKeyboardMarkupFromMap(rows));
        return message;
    }

    public SendMessage bindRatingQuestion(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                answerManager.getBasicAnswersList(RATING_YES, RATING_NO));
    }

    public SendMessage bindChooseRatingQuestion(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                answerManager.getRatingAnswersList());
    }

    public SendMessage bindChooseGenreQuestion(SendMessage message) {
        return bindInlineKeyboardFromMap(message,
                answerManager.getGenreAnswersMap());
    }

    public SendMessage bindGenreQuestion(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                answerManager.getBasicAnswersList(GENRE_YES, GENRE_NO));
    }

    public SendMessage bindYearQuestion(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                answerManager.getBasicAnswersList(YEAR_YES, YEAR_NO));

    }

    public SendMessage bindChooseYearQuestion(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                answerManager.getYearAnswersList());
    }

    public SendMessage bindCountryQuestion(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                answerManager.getBasicAnswersList(COUNTRY_YES, COUNTRY_NO));
    }

    public SendMessage bindChooseCountryQuestion(SendMessage message) {
        return bindInlineKeyboardFromMap(message,
                answerManager.getCountryAnswersMap());
    }
}
