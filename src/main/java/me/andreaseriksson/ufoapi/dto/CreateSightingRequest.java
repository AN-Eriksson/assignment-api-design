package me.andreaseriksson.ufoapi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateSightingRequest(
        LocalDateTime sightedAt,
        Integer durationSeconds,
        String durationText,
        String comments,
        LocalDate datePosted,
        Long locationId,
        Long shapeId
) {}
