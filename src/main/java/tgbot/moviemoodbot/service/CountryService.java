package tgbot.moviemoodbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tgbot.moviemoodbot.model.Country;
import tgbot.moviemoodbot.repository.CountryRepository;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public Country findByName(String name) {
        return countryRepository.findByName(name);
    }
}
