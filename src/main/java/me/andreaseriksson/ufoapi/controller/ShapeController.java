package me.andreaseriksson.ufoapi.controller;

import me.andreaseriksson.ufoapi.dto.ShapeMapper;
import me.andreaseriksson.ufoapi.dto.ShapeResponse;
import me.andreaseriksson.ufoapi.entity.Shape;
import me.andreaseriksson.ufoapi.service.ShapeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/shapes")
public class ShapeController {

    private static final int MAX_PAGE_SIZE = 100;
    private final ShapeService service;

    public ShapeController(ShapeService service) {
        this.service = service;
    }

    @GetMapping
    public CollectionModel<EntityModel<ShapeResponse>> getAll(
            @PageableDefault(size = 20)
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Pageable safePageable = enforcePageLimits(pageable);

        List<EntityModel<ShapeResponse>> models = service.findAll(safePageable)
                .map(ShapeMapper::toResponse)
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(ShapeController.class).getById(dto.id())).withSelfRel()
                ))
                .getContent();

        return CollectionModel.of(
                models,
                linkTo(ShapeController.class).withSelfRel(),
                linkTo(LocationController.class).withRel("locations"),
                linkTo(SightingController.class).withRel("sightings")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ShapeResponse>> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ShapeMapper::toResponse)
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(ShapeController.class).getById(id)).withSelfRel(),
                        linkTo(ShapeController.class).withRel("shapes")
                ))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchElementException("Sighting not found: " + id));
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
