package me.andreaseriksson.ufoapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import me.andreaseriksson.ufoapi.repository.SightingRepository;

@RestController
public class SightingController {

    private final SightingRepository repository;

    public SightingController(SightingRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/count")
    public long count() {
        return repository.count();
    }
}
