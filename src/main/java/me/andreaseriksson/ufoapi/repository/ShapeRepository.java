package me.andreaseriksson.ufoapi.repository;

import me.andreaseriksson.ufoapi.entity.Shape;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShapeRepository extends JpaRepository<Shape, Long> {

    Optional<Shape> findByName(String name);
}