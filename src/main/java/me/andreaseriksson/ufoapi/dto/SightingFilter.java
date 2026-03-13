package me.andreaseriksson.ufoapi.dto;

import java.time.LocalDate;

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
