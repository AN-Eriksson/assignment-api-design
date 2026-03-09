package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.entity.Sighting;
import me.andreaseriksson.ufoapi.repository.SightingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SightingService {

    private final SightingRepository repository;

    public SightingService(SightingRepository repository) {
        this.repository = repository;
    }

    public Sighting save(Sighting sighting) {
        return repository.save(sighting);
    }

    public List<Sighting> saveAll(List<Sighting> sightings) {
        return repository.saveAll(sightings);
    }

    public List<Sighting> findAll() {
        return repository.findAll();
    }

    public long count() {
        return repository.count();
    }
}
