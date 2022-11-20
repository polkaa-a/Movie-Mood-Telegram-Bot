package tgbot.moviemoodbot.questionnaire.buttons.enums;

import lombok.Getter;
import tgbot.moviemoodbot.questionnaire.buttons.Button;

public enum CountryButton implements Button {
    COUNTRY_ANY("Any"),
    COUNTRY_YES("Yes"),
    COUNTRY_NO("No");

    public static final String country = "COUNTRY_";

    @Getter
    private final String text;

    CountryButton(String text) {
        this.text = text;
    }

    public String getId() {
        return country + text;
    }
}
