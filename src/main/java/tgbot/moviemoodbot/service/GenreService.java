package tgbot.moviemoodbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tgbot.moviemoodbot.model.Genre;
import tgbot.moviemoodbot.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }
}
