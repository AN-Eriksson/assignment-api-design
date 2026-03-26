package me.andreaseriksson.ufoapi.dto;

/**
 * Response DTO returned after a successful user login.
 *
 * Contains the JWT token, token type, token expiration time (in milliseconds),
 * and basic user information such as user ID, username, and email.
 */
public record LoginResponse(
        String token,
        String tokenType,
        Long expiresIn,
        Long userId,
        String username,
        String email
) {
}
