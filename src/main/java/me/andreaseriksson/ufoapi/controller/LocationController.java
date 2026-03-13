package me.andreaseriksson.ufoapi.controller;

import me.andreaseriksson.ufoapi.entity.Location;
import me.andreaseriksson.ufoapi.service.LocationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final int MAX_PAGE_SIZE = 100;
    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @GetMapping
    public CollectionModel<EntityModel<Location>> getAll(
            @PageableDefault(size = 20)
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Pageable safePageable = enforcePageLimits(pageable);

        List<EntityModel<Location>> models = service.findAll(safePageable)
                .map(location -> EntityModel.of(location,
                        linkTo(methodOn(LocationController.class).getById(location.getId())).withSelfRel()
                ))
                .getContent();

        return CollectionModel.of(models,
                linkTo(LocationController.class).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Location>> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(location -> EntityModel.of(location,
                        linkTo(methodOn(LocationController.class).getById(id)).withSelfRel(),
                        linkTo(LocationController.class).withRel("locations")
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
}
