package me.andreaseriksson.ufoapi.dto;

import java.math.BigDecimal;

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
