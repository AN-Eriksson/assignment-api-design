package me.andreaseriksson.ufoapi.controller;

import me.andreaseriksson.ufoapi.service.SightingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SightingController {

    private final SightingService service;

    public SightingController(SightingService service) {
        this.service = service;
    }

    @GetMapping("/count")
    public long count() {
        return service.count();
    }
}
