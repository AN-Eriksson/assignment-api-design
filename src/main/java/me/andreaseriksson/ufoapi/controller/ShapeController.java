package me.andreaseriksson.ufoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import me.andreaseriksson.ufoapi.dto.ShapeMapper;
import me.andreaseriksson.ufoapi.dto.ShapeResponse;
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

    @Operation(
            summary = "Get all shapes",
            description = "Returns a paginated list of all UFO shapes with HATEOAS links. Supports paging, but sorting is always by 'id' ascending and any user-supplied sort is ignored.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of shapes",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"_embedded\": {\n" +
                                                    "    \"shapeResponseList\": [\n" +
                                                    "      {\n" +
                                                    "        \"id\": 1,\n" +
                                                    "        \"name\": \"cylinder\",\n" +
                                                    "        \"_links\": {\n" +
                                                    "          \"self\": { \"href\": \"/shapes/1\" }\n" +
                                                    "        }\n" +
                                                    "      }\n" +
                                                    "    ]\n" +
                                                    "  },\n" +
                                                    "  \"_links\": {\n" +
                                                    "    \"self\": { \"href\": \"/shapes\" },\n" +
                                                    "    \"locations\": { \"href\": \"/locations\" },\n" +
                                                    "    \"sightings\": { \"href\": \"/sightings\" }\n" +
                                                    "  }\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - authentication required",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-25T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 401,\n" +
                                                    "  \"error\": \"Unauthorized\",\n" +
                                                    "  \"message\": \"Authentication required or token is invalid\"\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public CollectionModel<EntityModel<ShapeResponse>> getAll(
            @PageableDefault(size = 20)
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Pageable safePageable = enforcePageLimits(pageable);

        // Always use fixed sort by id ascending, ignoring any user-supplied sort
        Pageable fixedSortPageable = PageRequest.of(
                safePageable.getPageNumber(),
                safePageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        List<EntityModel<ShapeResponse>> models = service.findAll(fixedSortPageable)
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

    @Operation(
            summary = "Get a shape by ID",
            description = "Returns a single UFO shape by its unique ID with HATEOAS links. If the shape does not exist, a 404 error is returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Shape found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"name\": \"cylinder\",\n" +
                                                    "  \"_links\": {\n" +
                                                    "    \"self\": { \"href\": \"/shapes/1\" },\n" +
                                                    "    \"shapes\": { \"href\": \"/shapes\" }\n" +
                                                    "  }\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Shape not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-25T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 404,\n" +
                                                    "  \"error\": \"Not Found\",\n" +
                                                    "  \"message\": \"Shape not found: 1\"\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - authentication required",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-25T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 401,\n" +
                                                    "  \"error\": \"Unauthorized\",\n" +
                                                    "  \"message\": \"Authentication required or token is invalid\"\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ShapeResponse>> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ShapeMapper::toResponse)
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(ShapeController.class).getById(id)).withSelfRel(),
                        linkTo(ShapeController.class).withRel("shapes")
                ))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchElementException("Shape not found: " + id));
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
