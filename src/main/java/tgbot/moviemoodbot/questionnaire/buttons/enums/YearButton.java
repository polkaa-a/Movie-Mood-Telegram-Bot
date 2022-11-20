package tgbot.moviemoodbot.questionnaire.buttons.enums;

import lombok.Getter;
import tgbot.moviemoodbot.questionnaire.buttons.Button;

public enum YearButton implements Button {
    YEAR_YES("Yes"),
    YEAR_NO("No"),

    YEAR_1900("1900"),
    YEAR_1920("1920"),
    YEAR_1940("1940"),
    YEAR_1960("1960"),
    YEAR_1980("1980"),
    YEAR_2000("2000"),
    YEAR_2010("2010"),
    YEAR_2020("2020"),
    YEAR_2030("2030");

    public static final String year = "YEAR_";

    @Getter
    private final String text;

    YearButton(String text) {
        this.text = text;
    }

    public String getId() {
        return year + text;
    }
}
