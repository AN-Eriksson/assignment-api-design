package me.andreaseriksson.ufoapi.dto;

import me.andreaseriksson.ufoapi.entity.Country;
import me.andreaseriksson.ufoapi.entity.Location;

/**
 * Utility class for mapping Location entities to LocationResponse DTOs.
 *
 * Provides a static method to convert a Location entity to its corresponding
 * LocationResponse, including extracting country information if available.
 *
 * This class is not intended to be instantiated.
 */
public final class LocationMapper {

    private LocationMapper() {
    }

    public static LocationResponse toResponse(Location location) {
        Country country = location.getCountry();

        return new LocationResponse(
                location.getId(),
                location.getCity(),
                location.getState(),
                country != null ? country.getCode() : null,
                country != null ? country.getId() : null,
                location.getLatitude(),
                location.getLongitude()
        );
    }
}
