package tgbot.moviemoodbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tgbot.moviemoodbot.model.Country;
import tgbot.moviemoodbot.model.Film;
import tgbot.moviemoodbot.model.Genre;
import tgbot.moviemoodbot.questionnaire.KeyboardManager;
import tgbot.moviemoodbot.repository.CountryRepository;
import tgbot.moviemoodbot.repository.FilmRepository;
import tgbot.moviemoodbot.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final CountryRepository countryRepository;
    private final GenreRepository genreRepository;
    private final KeyboardManager keyboardManager;

    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    public List<Film> findAllByRating(float rating) {
        return filmRepository.findAllByRatingGreaterThanEqual(rating);
    }

    public List<Film> findAllByGenre(Genre genre) {
        return filmRepository.findAllByGenresContains(genre);
    }

    public List<Film> findAllByYear(int year) {
        return filmRepository.findAllByYearLessThan(year);
    }

    public List<Film> findAllByCountry(Country country) {
        return filmRepository.findAllByCountriesContains(country);
    }


    public void filterCountry(String callbackData, long chatId) {

    }

    public void askCast() {

    }

    public void filterCast() {

    }

    public void askQuotes() {

    }

    public void filterQuotes() {

    }


}
