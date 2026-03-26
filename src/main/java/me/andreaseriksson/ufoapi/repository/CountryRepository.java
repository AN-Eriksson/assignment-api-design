package me.andreaseriksson.ufoapi.repository;

import me.andreaseriksson.ufoapi.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing Country entities.
 *
 * Extends JpaRepository to provide standard CRUD operations.
 * Includes a method to find a country by its code.
 */
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByCode(String code);
}
