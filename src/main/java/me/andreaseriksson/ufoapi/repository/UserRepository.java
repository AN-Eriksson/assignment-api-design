package me.andreaseriksson.ufoapi.repository;

import me.andreaseriksson.ufoapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing User entities.
 *
 * Extends JpaRepository to provide standard CRUD operations.
 * Includes methods to find users by username or email, and to check for their existence.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
