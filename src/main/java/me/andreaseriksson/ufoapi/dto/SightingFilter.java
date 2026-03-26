package me.andreaseriksson.ufoapi.dto;

import java.time.LocalDate;

/**
 * Filter criteria for searching UFO sightings.
 *
 * Contains optional fields for filtering sightings by city, state, country code,
 * shape name, duration range, and date posted range.
 *
 * Used to encapsulate search parameters in API requests.
 */
public record SightingFilter(
        String city,
        String state,
        String countryCode,
        String shapeName,
        Integer minDurationSeconds,
        Integer maxDurationSeconds,
        LocalDate fromDatePosted,
        LocalDate toDatePosted
) {
}
