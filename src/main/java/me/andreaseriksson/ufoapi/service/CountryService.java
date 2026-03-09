package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.entity.Country;
import me.andreaseriksson.ufoapi.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    private final CountryRepository repository;

    public CountryService(CountryRepository repository) {
        this.repository = repository;
    }

    public Country save(Country country) {
        return repository.save(country);
    }

    public List<Country> findAll() {
        return repository.findAll();
    }

    public Optional<Country> findByCode(String code) {
        return repository.findByCode(code);
    }
}
