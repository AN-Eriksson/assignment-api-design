package me.andreaseriksson.ufoapi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Request DTO for creating a new UFO sighting.
 *
 * Contains all required fields for submitting a new sighting:
 * - sightedAt: Date and time when the sighting occurred.
 * - durationSeconds: Duration of the sighting in seconds.
 * - durationText: Human-readable description of the duration.
 * - comments: Additional comments or description of the sighting.
 * - datePosted: Date when the sighting was reported.
 * - locationId: ID of the location where the sighting occurred.
 * - shapeId: ID of the reported UFO shape.
 */
public record CreateSightingRequest(
        LocalDateTime sightedAt,
        Integer durationSeconds,
        String durationText,
        String comments,
        LocalDate datePosted,
        Long locationId,
        Long shapeId
) {
}
