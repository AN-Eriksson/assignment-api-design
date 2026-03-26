package me.andreaseriksson.ufoapi.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for user login.
 *
 * Contains the login credential (username or email) and password required for authentication.
 * Both fields are mandatory.
 */
public record LoginRequest(
        @NotBlank(message = "Username or email is required")
        String login,

        @NotBlank(message = "Password is required")
        String password
) {
}
