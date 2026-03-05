package me.andreaseriksson.webapi.repository;

import me.andreaseriksson.webapi.entity.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SightingRepository extends JpaRepository<Sighting, Long> {
}
