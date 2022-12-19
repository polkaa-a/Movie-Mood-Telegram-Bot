package tgbot.moviemoodbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tgbot.moviemoodbot.model.Country;
import tgbot.moviemoodbot.model.Film;
import tgbot.moviemoodbot.model.Genre;
import tgbot.moviemoodbot.repository.FilmRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;

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

    public Film findByCastImageId(int id) {
        Film f = filmRepository.findByCastImageId(id);
        System.out.println("ID: " + f.getCastImageId());
        System.out.println("NAME: " + f.getRussianName());
        return f;
    }


}
