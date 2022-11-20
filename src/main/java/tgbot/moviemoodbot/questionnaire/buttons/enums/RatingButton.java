package tgbot.moviemoodbot.questionnaire.buttons.enums;

import lombok.Getter;
import tgbot.moviemoodbot.questionnaire.buttons.Button;

public enum RatingButton implements Button {

    RATING_YES("Yes"),
    RATING_NO("No"),

    RATING_5("5"),
    RATING_6("6"),
    RATING_7("7"),
    RATING_8("8"),
    RATING_9("9"),
    RATING_ANY("Any");

    public static final String rating = "RATING_";

    @Getter
    private final String text;

    RatingButton(String text) {
        this.text = text;
    }

    public String getId() {
        return rating + text;
    }
}
