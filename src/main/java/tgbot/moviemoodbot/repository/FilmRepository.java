package tgbot.moviemoodbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgbot.moviemoodbot.model.BotUser;
import tgbot.moviemoodbot.model.Country;
import tgbot.moviemoodbot.model.Film;
import tgbot.moviemoodbot.model.Genre;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilmRepository extends JpaRepository<Film, UUID> {

    Optional<Film> findByOriginalName(String name);

    Optional<Film> findByRussianName(String name);

    Optional<Film> findAllByYear(int year);

    Optional<Film> findAllByYearBetween(int firstYear, int secondYear);

    Optional<Film> findAllByRatingGreaterThanEqual(float rating);

    Optional<Film> findAllByCountriesContains(Country country);

    Optional<Film> findAllByGenresContains(Genre genre);

    Optional<Film> findAllByUsersNotContains(BotUser user);

}
