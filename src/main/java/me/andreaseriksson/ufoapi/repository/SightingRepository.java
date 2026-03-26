package me.andreaseriksson.ufoapi.repository;

import me.andreaseriksson.ufoapi.entity.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for accessing Sighting entities.
 *
 * Extends JpaRepository to provide standard CRUD operations and
 * JpaSpecificationExecutor to support dynamic filtering and complex queries.
 */
public interface SightingRepository extends JpaRepository<Sighting, Long>, JpaSpecificationExecutor<Sighting> {
}
