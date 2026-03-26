package me.andreaseriksson.ufoapi.dto;

/**
 * Response DTO representing a UFO shape type.
 *
 * Contains the shape's unique ID and name.
 * Used in API responses to provide shape information for sightings.
 */
public record ShapeResponse(
        Long id,
        String name
) {
}
