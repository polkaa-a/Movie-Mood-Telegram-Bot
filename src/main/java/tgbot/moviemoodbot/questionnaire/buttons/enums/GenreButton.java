package tgbot.moviemoodbot.questionnaire.buttons.enums;

import lombok.Getter;
import tgbot.moviemoodbot.questionnaire.buttons.Button;

public enum GenreButton implements Button {
    GENRE_YES("Yes"),
    GENRE_NO("No"),
    GENRE_ANY("Any");

    public static final String genre = "GENRE_";

    @Getter
    private final String text;

    GenreButton(String text) {
        this.text = text;
    }

    public String getId() {
        return genre + text;
    }
}
