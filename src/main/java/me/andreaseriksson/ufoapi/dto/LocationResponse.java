package me.andreaseriksson.ufoapi.dto;

import java.math.BigDecimal;

/**
 * Response DTO representing a location associated with a UFO sighting.
 *
 * Contains location details such as city, state, country code, country ID,
 * latitude, and longitude.
 *
 * Used in API responses to provide structured location information.
 */
public record LocationResponse(
        Long id,
        String city,
        String state,
        String countryCode,
        Long countryId,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
