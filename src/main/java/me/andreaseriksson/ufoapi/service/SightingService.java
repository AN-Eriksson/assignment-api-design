package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.entity.Sighting;
import me.andreaseriksson.ufoapi.repository.SightingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SightingService {

    private final SightingRepository sightingRepository;

    public SightingService(SightingRepository sightingRepository) {
        this.sightingRepository = sightingRepository;
    }

    public Sighting save(Sighting sighting) {
        return sightingRepository.save(sighting);
    }

    public List<Sighting> saveAll(List<Sighting> sightings) {
        return sightingRepository.saveAll(sightings);
    }

    public List<Sighting> findAll() {
        return sightingRepository.findAll();
    }

    public long count() {
        return sightingRepository.count();
    }
}
