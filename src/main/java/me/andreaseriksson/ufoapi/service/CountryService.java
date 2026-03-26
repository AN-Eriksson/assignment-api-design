package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.entity.Country;
import me.andreaseriksson.ufoapi.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing Country entities.
 *
 * Provides methods to save a country, retrieve all countries, and find a country by its code.
 * Acts as an intermediary between the CountryRepository and higher-level application logic.
 */
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
