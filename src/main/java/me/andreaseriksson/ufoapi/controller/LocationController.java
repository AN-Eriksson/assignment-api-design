package me.andreaseriksson.ufoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import me.andreaseriksson.ufoapi.dto.LocationMapper;
import me.andreaseriksson.ufoapi.dto.LocationResponse;
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
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final int MAX_PAGE_SIZE = 100;
    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all locations",
            description = "Returns a paginated list of all locations with HATEOAS links. Supports paging, but sorting is not supported: results are always sorted by 'id' ascending, regardless of any 'sort' parameter.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of locations",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"_embedded\": {\n" +
                                                    "    \"locationResponseList\": [\n" +
                                                    "      {\n" +
                                                    "        \"id\": 1,\n" +
                                                    "        \"city\": \"san marcos\",\n" +
                                                    "        \"state\": \"tx\",\n" +
                                                    "        \"countryCode\": \"us\",\n" +
                                                    "        \"latitude\": 29.88,\n" +
                                                    "        \"longitude\": -97.94,\n" +
                                                    "        \"_links\": {\n" +
                                                    "          \"self\": { \"href\": \"/locations/1\" }\n" +
                                                    "        }\n" +
                                                    "      }\n" +
                                                    "    ]\n" +
                                                    "  },\n" +
                                                    "  \"_links\": {\n" +
                                                    "    \"self\": { \"href\": \"/locations\" },\n" +
                                                    "    \"shapes\": { \"href\": \"/shapes\" },\n" +
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
    public CollectionModel<EntityModel<LocationResponse>> getAll(
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

        List<EntityModel<LocationResponse>> models = service.findAll(fixedSortPageable)
                .map(LocationMapper::toResponse)
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(LocationController.class).getById(dto.id())).withSelfRel()
                ))
                .getContent();

        return CollectionModel.of(
                models,
                linkTo(LocationController.class).withSelfRel(),
                linkTo(ShapeController.class).withRel("shapes"),
                linkTo(SightingController.class).withRel("sightings")
        );
    }

    @Operation(
            summary = "Get a location by ID",
            description = "Returns a single location by its unique ID with HATEOAS links. If the location does not exist, a 404 error is returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Location found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"city\": \"san marcos\",\n" +
                                                    "  \"state\": \"tx\",\n" +
                                                    "  \"countryCode\": \"us\",\n" +
                                                    "  \"latitude\": 29.88,\n" +
                                                    "  \"longitude\": -97.94,\n" +
                                                    "  \"_links\": {\n" +
                                                    "    \"self\": { \"href\": \"/locations/1\" },\n" +
                                                    "    \"locations\": { \"href\": \"/locations\" }\n" +
                                                    "  }\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Location not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-25T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 404,\n" +
                                                    "  \"error\": \"Not Found\",\n" +
                                                    "  \"message\": \"Location not found: 1\"\n" +
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
    public ResponseEntity<EntityModel<LocationResponse>> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(LocationMapper::toResponse)
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(LocationController.class).getById(id)).withSelfRel(),
                        linkTo(LocationController.class).withRel("locations")
                ))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchElementException("Location not found: " + id));
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
