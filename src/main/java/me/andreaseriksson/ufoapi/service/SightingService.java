package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.entity.Sighting;
import me.andreaseriksson.ufoapi.repository.SightingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Sighting> findById(Long id) {
        return repository.findById(id);
    }

    public List<Sighting> findAll() {
        return repository.findAll();
    }

    public Optional<Sighting> update(Long id, Sighting incoming) {
        return repository.findById(id).map(existing -> {
            existing.setSightedAt(incoming.getSightedAt());
            existing.setDurationSeconds(incoming.getDurationSeconds());
            existing.setDurationText(incoming.getDurationText());
            existing.setComments(incoming.getComments());
            existing.setDatePosted(incoming.getDatePosted());
            existing.setLocation(incoming.getLocation());
            existing.setShape(incoming.getShape());
            return repository.save(existing);
        });
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    public long count() {
        return repository.count();
    }
}
