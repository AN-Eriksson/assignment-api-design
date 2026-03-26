package me.andreaseriksson.ufoapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for user registration.
 *
 * Contains the required fields for creating a new user account:
 * - username: Must be 5–50 characters.
 * - email: Must be a valid email address.
 * - password: Must be at least 8 characters.
 *
 * All fields are mandatory and validated.
 */
public record RegisterRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 5, max = 50, message = "Username must be 5–50 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password

) {
}
