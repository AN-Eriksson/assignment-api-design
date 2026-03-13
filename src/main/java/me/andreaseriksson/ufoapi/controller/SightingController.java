package me.andreaseriksson.ufoapi.controller;

import me.andreaseriksson.ufoapi.entity.Sighting;
import me.andreaseriksson.ufoapi.service.SightingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sightings")
public class SightingController {

    private final SightingService service;

    public SightingController(SightingService service) {
        this.service = service;
    }

    @GetMapping
    public List<Sighting> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sighting> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(sighting -> ResponseEntity.ok(sighting))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sighting> create(@RequestBody Sighting sighting) {
        Sighting created = service.save(sighting);
        return ResponseEntity.created(URI.create("/sightings/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sighting> update(@PathVariable Long id, @RequestBody Sighting sighting) {
        return service.update(id, sighting)
                .map(updated -> ResponseEntity.ok(updated))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public long count() {
        return service.count();
    }
}
