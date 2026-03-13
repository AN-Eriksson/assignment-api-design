package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.dto.CreateSightingRequest;
import me.andreaseriksson.ufoapi.entity.Location;
import me.andreaseriksson.ufoapi.entity.Shape;
import me.andreaseriksson.ufoapi.entity.Sighting;
import me.andreaseriksson.ufoapi.repository.SightingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SightingService {

    private final SightingRepository repository;
    private final LocationService locationService;
    private final ShapeService shapeService;

    public SightingService(SightingRepository repository, LocationService locationService, ShapeService shapeService) {
        this.repository = repository;
        this.locationService = locationService;
        this.shapeService = shapeService;
    }

    public Sighting create(CreateSightingRequest req) {
        Location location = locationService.findById(req.locationId())
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        Shape shape = shapeService.findById(req.shapeId())
                .orElseThrow(() -> new IllegalArgumentException("Shape not found"));

        Sighting s = new Sighting();
        s.setSightedAt(req.sightedAt());
        s.setDurationSeconds(req.durationSeconds());
        s.setDurationText(req.durationText());
        s.setComments(req.comments());
        s.setDatePosted(req.datePosted());
        s.setLocation(location);
        s.setShape(shape);

        return repository.save(s);
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

    public Optional<Sighting> update(Long id, CreateSightingRequest req) {
        return repository.findById(id).map(existing -> {
            Location location = locationService.findById(req.locationId())
                    .orElseThrow(() -> new IllegalArgumentException("Location not found"));
            Shape shape = shapeService.findById(req.shapeId())
                    .orElseThrow(() -> new IllegalArgumentException("Shape not found"));

            existing.setSightedAt(req.sightedAt());
            existing.setDurationSeconds(req.durationSeconds());
            existing.setDurationText(req.durationText());
            existing.setComments(req.comments());
            existing.setDatePosted(req.datePosted());
            existing.setLocation(location);
            existing.setShape(shape);

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
