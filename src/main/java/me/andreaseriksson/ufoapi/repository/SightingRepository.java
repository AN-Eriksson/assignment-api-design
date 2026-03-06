package me.andreaseriksson.ufoapi.repository;

import me.andreaseriksson.ufoapi.entity.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SightingRepository extends JpaRepository<Sighting, Long> {
}
