package me.andreaseriksson.ufoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import me.andreaseriksson.ufoapi.dto.LoginRequest;
import me.andreaseriksson.ufoapi.dto.LoginResponse;
import me.andreaseriksson.ufoapi.dto.RegisterRequest;
import me.andreaseriksson.ufoapi.dto.RegisterResponse;
import me.andreaseriksson.ufoapi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user account. Returns a success message and user details. If the input is invalid or the username/email is already taken, a 400 error is returned.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"username\": \"newuser\",\n" +
                                            "  \"email\": \"newuser@example.com\",\n" +
                                            "  \"password\": \"strongPassword123\"\n" +
                                            "}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registered",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"id\": 1001,\n" +
                                                    "  \"username\": \"newuser\",\n" +
                                                    "  \"email\": \"newuser@example.com\",\n" +
                                                    "  \"message\": \"Registration successful\"\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or user already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 400,\n" +
                                                    "  \"error\": \"Bad Request\",\n" +
                                                    "  \"message\": \"Username or email already exists\"\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest req) {
        RegisterResponse response = authService.register(req);

        return ResponseEntity.status(201).body(response);
    }

    @Operation(
            summary = "Authenticate user and return JWT",
            description = "Authenticates a user using either username or email in the 'login' field, and returns a JWT token with user details if credentials are valid. If credentials are invalid, a 401 error is returned.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"login\": \"username_or_email\",\n" +
                                            "  \"password\": \"strongPassword123\"\n" +
                                            "}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authentication successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"token\": \"eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhbmRyYXNkYXNhc2RlYXMiLCJ1c2VySWQiOjEwMiwiaWF0IjoxNzc0MzgxNDkyLCJleHAiOjE3NzQzODUwOTJ9.atFvTHDyuBjlMdvgjKdgnYjBzjvMJtKPq8FIrmNm1Bw4LeC2-46QqtlOmGO0-ai3\",\n" +
                                                    "  \"tokenType\": \"Bearer\",\n" +
                                                    "  \"expiresIn\": 3600000,\n" +
                                                    "  \"userId\": 102,\n" +
                                                    "  \"username\": \"username\",\n" +
                                                    "  \"email\": \"mail@email.com\"\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid credentials",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-03-24T19:00:00.000+00:00\",\n" +
                                                    "  \"status\": 401,\n" +
                                                    "  \"error\": \"Unauthorized\",\n" +
                                                    "  \"message\": \"Invalid username/email or password\"\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}