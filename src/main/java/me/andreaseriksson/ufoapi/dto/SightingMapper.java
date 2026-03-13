package me.andreaseriksson.ufoapi.dto;

import me.andreaseriksson.ufoapi.entity.Country;
import me.andreaseriksson.ufoapi.entity.Location;
import me.andreaseriksson.ufoapi.entity.Shape;
import me.andreaseriksson.ufoapi.entity.Sighting;

public final class SightingMapper {

    private SightingMapper() {
    }

    public static SightingResponse toResponse(Sighting s) {
        Location location = s.getLocation();
        Shape shape = s.getShape();
        Country country = location != null ? location.getCountry() : null;

        return new SightingResponse(
                s.getId(),
                s.getSightedAt(),
                s.getDurationSeconds(),
                s.getDurationText(),
                s.getComments(),
                s.getDatePosted(),
                location != null ? location.getCity() : null,
                location != null ? location.getState() : null,
                country != null ? country.getCode() : null,
                location != null ? location.getLatitude() : null,
                location != null ? location.getLongitude() : null,
                shape != null ? shape.getName() : null,
                location != null ? location.getId() : null,
                shape != null ? shape.getId() : null
        );
    }
}
