package me.andreaseriksson.ufoapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO representing a UFO sighting.
 *
 * Contains all relevant details about a sighting, including:
 * - id: Unique identifier for the sighting.
 * - sightedAt: Date and time when the sighting occurred.
 * - durationSeconds: Duration of the sighting in seconds.
 * - durationText: Human-readable description of the duration.
 * - comments: Additional comments or description of the sighting.
 * - datePosted: Date when the sighting was reported.
 * - city: City where the sighting occurred.
 * - state: State or region of the sighting.
 * - countryCode: Country code of the sighting location.
 * - latitude: Latitude coordinate of the sighting.
 * - longitude: Longitude coordinate of the sighting.
 * - shapeName: Name of the reported UFO shape.
 * - locationId: ID of the associated location (internal reference).
 * - shapeId: ID of the associated shape (internal reference).
 *
 * Used in API responses to provide structured sighting information.
 */
public record SightingResponse(
        Long id,
        LocalDateTime sightedAt,     // csv: datetime
        Integer durationSeconds,     // csv: duration (seconds)
        String durationText,         // csv: duration (hours/min)
        String comments,             // csv: comments
        LocalDate datePosted,        // csv: date posted
        String city,                 // csv: city
        String state,                // csv: state
        String countryCode,          // csv: country
        BigDecimal latitude,         // csv: latitude
        BigDecimal longitude,        // csv: longitude
        String shapeName,            // csv: shape
        Long locationId,             // extra (not csv)
        Long shapeId                 // extra (not csv)
) {
    public Long getShapeId() {
        return shapeId;
    }

    public Long getLocationId() {
        return locationId;
    }
}
