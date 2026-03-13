package me.andreaseriksson.ufoapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
) {}
