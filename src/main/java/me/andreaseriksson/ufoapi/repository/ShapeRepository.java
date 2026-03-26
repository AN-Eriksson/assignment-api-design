package me.andreaseriksson.ufoapi.repository;

import me.andreaseriksson.ufoapi.entity.Shape;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing Shape entities.
 *
 * Extends JpaRepository to provide standard CRUD operations.
 * Includes a method to find a shape by its name.
 */
public interface ShapeRepository extends JpaRepository<Shape, Long> {

    Optional<Shape> findByName(String name);
}