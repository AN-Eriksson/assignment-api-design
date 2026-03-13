package me.andreaseriksson.ufoapi.controller;

import me.andreaseriksson.ufoapi.dto.CreateSightingRequest;
import me.andreaseriksson.ufoapi.dto.SightingMapper;
import me.andreaseriksson.ufoapi.dto.SightingResponse;
import me.andreaseriksson.ufoapi.entity.Sighting;
import me.andreaseriksson.ufoapi.service.SightingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sightings")
public class SightingController {

    private static final int MAX_PAGE_SIZE = 100;
    private final SightingService service;

    public SightingController(SightingService service) {
        this.service = service;
    }

    @GetMapping
    public List<SightingResponse> getAll(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Pageable safePageable = enforcePageLimits(pageable);
        return service.findAll(safePageable)
                .map(SightingMapper::toResponse)
                .getContent();
    }

    private Pageable enforcePageLimits(Pageable pageable) {
        int safePage = Math.max(pageable.getPageNumber(), 0);
        int requestedSize = pageable.getPageSize();
        int safeSize = Math.min(Math.max(requestedSize, 1), MAX_PAGE_SIZE);

        Sort sort = pageable.getSort().isSorted()
                ? pageable.getSort()
                : Sort.by(Sort.Direction.ASC, "id");

        return PageRequest.of(safePage, safeSize, sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SightingResponse> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(SightingMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SightingResponse> create(@RequestBody CreateSightingRequest req) {
        Sighting created = service.create(req);
        return ResponseEntity.created(URI.create("/sightings/" + created.getId()))
                .body(SightingMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SightingResponse> update(@PathVariable Long id, @RequestBody CreateSightingRequest req) {
        return service.update(id, req)
                .map(SightingMapper::toResponse)
                .map(ResponseEntity::ok)
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
