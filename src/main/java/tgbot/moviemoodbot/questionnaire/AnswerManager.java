package tgbot.moviemoodbot.questionnaire;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tgbot.moviemoodbot.model.Country;
import tgbot.moviemoodbot.model.Genre;
import tgbot.moviemoodbot.questionnaire.buttons.Button;
import tgbot.moviemoodbot.questionnaire.buttons.enums.CountryButton;
import tgbot.moviemoodbot.questionnaire.buttons.enums.GenreButton;
import tgbot.moviemoodbot.questionnaire.buttons.enums.RatingButton;
import tgbot.moviemoodbot.questionnaire.buttons.enums.YearButton;
import tgbot.moviemoodbot.repository.CountryRepository;
import tgbot.moviemoodbot.repository.GenreRepository;

import java.util.*;

import static tgbot.moviemoodbot.questionnaire.buttons.enums.CountryButton.COUNTRY_ANY;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.GenreButton.GENRE_ANY;

@Component
@RequiredArgsConstructor
public class AnswerManager {

    private static final String YES = "Yes";
    private static final String NO = "No";

    private final GenreRepository genreRepository;
    private final CountryRepository countryRepository;

    public List<List<Button>> getBasicAnswersList(Button yes, Button no) {
        List<Button> list = new ArrayList<>(List.of(new Button[]{yes, no}));
        return new ArrayList<>(Collections.singleton(list));
    }

    public List<List<Button>> getRatingAnswersList() {
        List<Button> firstRow = new ArrayList<>();
        List<Button> secondRow = new ArrayList<>();
        for (RatingButton button : RatingButton.values()) {
            if (!button.getText().equals(YES) && !button.getText().equals(NO)) {
                if (button != RatingButton.RATING_ANY) {
                    firstRow.add(button);
                } else secondRow.add(button);
            }
        }
        List<List<Button>> buttons = new ArrayList<>();
        buttons.add(firstRow);
        buttons.add(secondRow);

        return buttons;
    }

    public List<List<Button>> getYearAnswersList() {
        List<Button> row = new ArrayList<>();
        for (YearButton button : YearButton.values()) {
            if (!button.getText().equals(YES) && !button.getText().equals(NO)) {
                row.add(button);
            }
        }
        List<List<Button>> buttons = new ArrayList<>();
        buttons.add(row);

        return buttons;
    }

    public List<Map<String, String>> getCountryAnswersMap() {
        List<Country> countries = countryRepository.findAll();
        List<Map<String, String>> list = new ArrayList<>();

        for (Country country : countries) {
            Map<String, String> map = new HashMap<>();
            map.put(CountryButton.country + country.getName(), country.getName());
            list.add(map);
        }

        Map<String, String> any = new HashMap<>();
        any.put(COUNTRY_ANY.getId(), COUNTRY_ANY.getText());
        list.add(any);
        return list;
    }

    public List<Map<String, String>> getGenreAnswersMap() {
        List<Genre> genres = genreRepository.findAll();
        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < genres.size(); i += 2) {
            if (i == genres.size() - 1) {
                Map<String, String> last = new HashMap<>();
                Genre current = genres.get(i);
                last.put(GenreButton.genre + current.getName(), current.getName());
                list.add(last);
            } else list.add(splitGenres(genres, i));
        }

        Map<String, String> any = new HashMap<>();
        any.put(GENRE_ANY.getId(), GENRE_ANY.getText());
        list.add(any);
        return list;
    }

    private Map<String, String> splitGenres(List<Genre> genres, int index) {
        Map<String, String> map = new HashMap<>();
        Genre current = genres.get(index);
        Genre next = genres.get(index + 1);

        map.put(GenreButton.genre + current.getName(), current.getName());
        map.put(GenreButton.genre + next.getName(), next.getName());

        return map;
    }
}
