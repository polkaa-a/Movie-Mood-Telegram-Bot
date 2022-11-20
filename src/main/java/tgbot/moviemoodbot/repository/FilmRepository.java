package tgbot.moviemoodbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgbot.moviemoodbot.model.BotUser;
import tgbot.moviemoodbot.model.Country;
import tgbot.moviemoodbot.model.Film;
import tgbot.moviemoodbot.model.Genre;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findByOriginalName(String name);

    List<Film> findByRussianName(String name);

    List<Film> findAllByYear(int year);

    List<Film> findAllByYearBetween(int firstYear, int secondYear);

    List<Film> findAllByYearLessThan(int year);

    List<Film> findAllByRatingGreaterThanEqual(float rating);

    List<Film> findAllByCountriesContains(Country country);

    List<Film> findAllByGenresContains(Genre genre);

    List<Film> findAllByUsersNotContains(BotUser user);

}
