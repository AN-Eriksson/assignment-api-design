package me.andreaseriksson.ufoapi.dto;

/**
 * Response DTO returned after successful user registration.
 *
 * Contains the new user's ID, username, and email address.
 */
public record RegisterResponse(
        Long id,
        String username,
        String email
) {
}
