package me.andreaseriksson.ufoapi.repository;

import me.andreaseriksson.ufoapi.entity.Country;
import me.andreaseriksson.ufoapi.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Repository interface for accessing Location entities.
 *
 * Extends JpaRepository to provide standard CRUD operations.
 * Includes a method to find a location by city, state, country, latitude, and longitude.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByCityAndStateAndCountryAndLatitudeAndLongitude(
            String city,
            String state,
            Country country,
            BigDecimal latitude,
            BigDecimal longitude
    );

}
