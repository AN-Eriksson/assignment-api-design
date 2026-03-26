package me.andreaseriksson.ufoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.andreaseriksson.ufoapi.dto.CreateSightingRequest;
import me.andreaseriksson.ufoapi.dto.SightingFilter;
import me.andreaseriksson.ufoapi.dto.SightingMapper;
import me.andreaseriksson.ufoapi.dto.SightingResponse;
import me.andreaseriksson.ufoapi.entity.Sighting;
import me.andreaseriksson.ufoapi.service.SightingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/sightings")
@Tag(
        name = "Sightings",
        description = """
                Endpoints for managing UFO sightings.
                - GET /sightings and GET /sightings/{id}: Public, no authentication required.
                - POST, PUT, DELETE on /sightings: Require authentication.
                - Supports filtering and paging. Sorting is fixed by id.
                """
)
public class SightingController {
    private static final int MAX_PAGE_SIZE = 100;
    private final SightingService service;

    public SightingController(SightingService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all sightings",
            description = "Returns a list of UFO sightings with optional filters and paging. Sorting is not supported: results are always sorted by 'id' ascending, regardless of any 'sort' parameter.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "List of sightings",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n  \"_embedded\": {\n    \"sightingResponseList\": [\n      {\n        \"_links\": {\n          \"self\": { \"href\": \"/sightings/1\" }\n        },\n        \"id\": 1,\n        \"sightedAt\": \"1949-10-10T20:30:00\",\n        \"durationSeconds\": 2700,\n        \"durationText\": \"45 minutes\",\n        \"comments\": \"This event took place in early fall around 1949-50. It occurred after a Boy Scout meeting in the Baptist Church. The Baptist Church sit\",\n        \"datePosted\": \"2004-04-27\",\n        \"city\": \"san marcos\",\n        \"state\": \"tx\",\n        \"countryCode\": \"us\",\n        \"latitude\": 29.88,\n        \"longitude\": -97.94,\n        \"shapeName\": \"cylinder\",\n        \"locationId\": 1,\n        \"shapeId\": 1\n      }\n    ]\n  },\n  \"_links\": {\n    \"self\": { \"href\": \"/sightings\" },\n    \"shapes\": { \"href\": \"/shapes\" },\n    \"locations\": { \"href\": \"/locations\" }\n  }\n}"
                            )
                    )
            )
    )
    @GetMapping
    public CollectionModel<EntityModel<SightingResponse>> getAll(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String shapeName,
            @RequestParam(required = false) Integer minDurationSeconds,
            @RequestParam(required = false) Integer maxDurationSeconds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDatePosted,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDatePosted,
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

        SightingFilter filter = new SightingFilter(
                city, state, countryCode, shapeName,
                minDurationSeconds, maxDurationSeconds,
                fromDatePosted, toDatePosted
        );

        List<EntityModel<SightingResponse>> models = service.findAll(fixedSortPageable, filter)
                .map(SightingMapper::toResponse)
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(SightingController.class).getById(dto.id())).withSelfRel()
                ))
                .getContent();

        return CollectionModel.of(models,
                linkTo(SightingController.class).withSelfRel(),
                linkTo(ShapeController.class).withRel("shapes"),
                linkTo(LocationController.class).withRel("locations")
        );

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

    @Operation(
            summary = "Get a sighting by ID",
            description = "Returns a single UFO sighting by its unique ID. The response includes HATEOAS links. If the sighting does not exist, a 404 error is returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sighting found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"sightedAt\": \"1949-10-10T20:30:00\",\n" +
                                                    "  \"durationSeconds\": 2700,\n" +
                                                    "  \"durationText\": \"45 minutes\",\n" +
                                                    "  \"comments\": \"This event took place in early fall around 1949-50. It occurred after a Boy Scout meeting in the Baptist Church. The Baptist Church sit\",\n" +
                                                    "  \"datePosted\": \"2004-04-27\",\n" +
                                                    "  \"city\": \"san marcos\",\n" +
                                                    "  \"state\": \"tx\",\n" +
                                                    "  \"countryCode\": \"us\",\n" +
                                                    "  \"latitude\": 29.88,\n" +
                                                    "  \"longitude\": -97.94,\n" +
                                                    "  \"shapeName\": \"cylinder\",\n" +
                                                    "  \"locationId\": 1,\n" +
                                                    "  \"shapeId\": 1,\n" +
                                                    "  \"_links\": {\n" +
                                                    "    \"self\": { \"href\": \"/sightings/1\" },\n" +
                                                    "    \"sightings\": { \"href\": \"/sightings\" },\n" +
                                                    "    \"shape\": { \"href\": \"/shapes/1\" },\n" +
                                                    "    \"location\": { \"href\": \"/locations/1\" }\n" +
                                                    "  }\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sighting not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 404,\n" +
                                                    "  \"error\": \"Not Found\",\n" +
                                                    "  \"message\": \"Sighting not found: 123\"\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SightingResponse>> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(SightingMapper::toResponse)
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(SightingController.class).getById(id)).withSelfRel(),
                        linkTo(SightingController.class).withRel("sightings"),
                        linkTo(methodOn(ShapeController.class).getById(dto.getShapeId())).withRel("shape"),
                        linkTo(methodOn(LocationController.class).getById(dto.getLocationId())).withRel("location")
                ))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchElementException("Sighting not found: " + id));
    }

    @Operation(
            summary = "Create a new sighting",
            description = "Creates a new UFO sighting. The request body must contain all required fields. Returns the created sighting with HATEOAS links. If the input is invalid, a 400 error is returned.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"sightedAt\": \"2026-03-24T18:57:28.218Z\",\n" +
                                            "  \"durationSeconds\": 2700,\n" +
                                            "  \"durationText\": \"45 minutes\",\n" +
                                            "  \"comments\": \"Saw a bright light in the sky.\",\n" +
                                            "  \"datePosted\": \"2026-03-24\",\n" +
                                            "  \"city\": \"san marcos\",\n" +
                                            "  \"state\": \"tx\",\n" +
                                            "  \"countryCode\": \"us\",\n" +
                                            "  \"latitude\": 29.88,\n" +
                                            "  \"longitude\": -97.94,\n" +
                                            "  \"shapeName\": \"cylinder\",\n" +
                                            "  \"locationId\": 1,\n" +
                                            "  \"shapeId\": 1\n" +
                                            "}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Sighting created",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"id\": 42,\n" +
                                                    "  \"sightedAt\": \"2026-03-24T18:57:28.218Z\",\n" +
                                                    "  \"durationSeconds\": 2700,\n" +
                                                    "  \"durationText\": \"45 minutes\",\n" +
                                                    "  \"comments\": \"Saw a bright light in the sky.\",\n" +
                                                    "  \"datePosted\": \"2026-03-24\",\n" +
                                                    "  \"city\": \"san marcos\",\n" +
                                                    "  \"state\": \"tx\",\n" +
                                                    "  \"countryCode\": \"us\",\n" +
                                                    "  \"latitude\": 29.88,\n" +
                                                    "  \"longitude\": -97.94,\n" +
                                                    "  \"shapeName\": \"cylinder\",\n" +
                                                    "  \"locationId\": 1,\n" +
                                                    "  \"shapeId\": 1,\n" +
                                                    "  \"_links\": {\n" +
                                                    "    \"self\": { \"href\": \"/sightings/42\" },\n" +
                                                    "    \"sightings\": { \"href\": \"/sightings\" },\n" +
                                                    "    \"shape\": { \"href\": \"/shapes/1\" },\n" +
                                                    "    \"location\": { \"href\": \"/locations/1\" }\n" +
                                                    "  }\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 400,\n" +
                                                    "  \"error\": \"Bad Request\",\n" +
                                                    "  \"message\": \"Validation failed for object: 'createSightingRequest'. Field error in object...\"\n" +
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
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 401,\n" +
                                                    "  \"error\": \"Unauthorized\",\n" +
                                                    "  \"message\": \"Authentication required or token is invalid\"\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<EntityModel<SightingResponse>> create(@RequestBody CreateSightingRequest req) {
        Sighting created = service.create(req);
        SightingResponse dto = SightingMapper.toResponse(created);
        Long newId = created.getId();

        EntityModel<SightingResponse> model = EntityModel.of(dto,
                linkTo(methodOn(SightingController.class).getById(newId)).withSelfRel(),
                linkTo(SightingController.class).withRel("sightings"),
                linkTo(methodOn(ShapeController.class).getById(dto.getShapeId())).withRel("shape"),
                linkTo(methodOn(LocationController.class).getById(dto.getLocationId())).withRel("location")
        );

        return ResponseEntity
                .created(linkTo(methodOn(SightingController.class).getById(newId)).toUri())
                .body(model);
    }

    @Operation(
            summary = "Update a sighting by ID",
            description = "Updates an existing UFO sighting by its unique ID. Requires authentication. Returns the updated sighting with HATEOAS links. If the input is invalid, a 400 error is returned. If the sighting does not exist, a 404 error is returned. If not authenticated, a 401 error is returned.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"sightedAt\": \"2026-03-24T18:57:28.218Z\",\n" +
                                            "  \"durationSeconds\": 2700,\n" +
                                            "  \"durationText\": \"45 minutes\",\n" +
                                            "  \"comments\": \"Updated sighting details.\",\n" +
                                            "  \"datePosted\": \"2026-03-24\",\n" +
                                            "  \"city\": \"san marcos\",\n" +
                                            "  \"state\": \"tx\",\n" +
                                            "  \"countryCode\": \"us\",\n" +
                                            "  \"latitude\": 29.88,\n" +
                                            "  \"longitude\": -97.94,\n" +
                                            "  \"shapeName\": \"cylinder\",\n" +
                                            "  \"locationId\": 1,\n" +
                                            "  \"shapeId\": 1\n" +
                                            "}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sighting updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"id\": 42,\n" +
                                                    "  \"sightedAt\": \"2026-03-24T18:57:28.218Z\",\n" +
                                                    "  \"durationSeconds\": 2700,\n" +
                                                    "  \"durationText\": \"45 minutes\",\n" +
                                                    "  \"comments\": \"Updated sighting details.\",\n" +
                                                    "  \"datePosted\": \"2026-03-24\",\n" +
                                                    "  \"city\": \"san marcos\",\n" +
                                                    "  \"state\": \"tx\",\n" +
                                                    "  \"countryCode\": \"us\",\n" +
                                                    "  \"latitude\": 29.88,\n" +
                                                    "  \"longitude\": -97.94,\n" +
                                                    "  \"shapeName\": \"cylinder\",\n" +
                                                    "  \"locationId\": 1,\n" +
                                                    "  \"shapeId\": 1,\n" +
                                                    "  \"_links\": {\n" +
                                                    "    \"self\": { \"href\": \"/sightings/42\" },\n" +
                                                    "    \"sightings\": { \"href\": \"/sightings\" },\n" +
                                                    "    \"shape\": { \"href\": \"/shapes/1\" },\n" +
                                                    "    \"location\": { \"href\": \"/locations/1\" }\n" +
                                                    "  }\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 400,\n" +
                                                    "  \"error\": \"Bad Request\",\n" +
                                                    "  \"message\": \"Validation failed for object: 'createSightingRequest'. Field error in object...\"\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sighting not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 404,\n" +
                                                    "  \"error\": \"Not Found\",\n" +
                                                    "  \"message\": \"Sighting not found: 42\"\n" +
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
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 401,\n" +
                                                    "  \"error\": \"Unauthorized\",\n" +
                                                    "  \"message\": \"Authentication required or token is invalid\"\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SightingResponse>> update(@PathVariable Long id, @RequestBody CreateSightingRequest req) {
        return service.update(id, req)
                .map(SightingMapper::toResponse)
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(SightingController.class).getById(id)).withSelfRel(),
                        linkTo(SightingController.class).withRel("sightings"),
                        linkTo(methodOn(ShapeController.class).getById(dto.getShapeId())).withRel("shape"),
                        linkTo(methodOn(LocationController.class).getById(dto.getLocationId())).withRel("location")
                ))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchElementException("Sighting not found: " + id));
    }

    @Operation(
            summary = "Delete a sighting by ID",
            description = "Deletes an existing UFO sighting by its unique ID. Requires authentication. Returns 204 No Content on success. If the sighting does not exist, a 404 error is returned. If not authenticated, a 401 error is returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Sighting deleted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sighting not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 404,\n" +
                                                    "  \"error\": \"Not Found\",\n" +
                                                    "  \"message\": \"Sighting not found: 42\"\n" +
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
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 401,\n" +
                                                    "  \"error\": \"Unauthorized\",\n" +
                                                    "  \"message\": \"Authentication required or token is invalid\"\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.existsById(id)) {
            throw new NoSuchElementException("Sighting not found: " + id);
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get total count of sightings",
            description = "Returns the total number of UFO sightings in the database. Requires authentication. If not authenticated, a 401 error is returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Total count of sightings",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "12345"
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
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 401,\n" +
                                                    "  \"error\": \"Unauthorized\",\n" +
                                                    "  \"message\": \"Authentication required or token is invalid\"\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/count")
    public long count() {
        return service.count();
    }
}
