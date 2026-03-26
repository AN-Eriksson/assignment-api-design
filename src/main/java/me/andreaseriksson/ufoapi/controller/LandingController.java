package me.andreaseriksson.ufoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller that provides the API landing page.
 *
 * Exposes a GET endpoint at "/" that returns a summary of available API resources,
 * their URLs, and access requirements. This helps users discover the main endpoints
 * and understand which operations require authentication.
 *
 * The landing page is also documented with OpenAPI annotations for clear Swagger UI display.
 */
@RestController
@RequestMapping("/")
@Tag(name = "API Landing Page", description = "Overview of available API resources and their access requirements.")
public class LandingController {

    @Operation(
            summary = "API Landing Page",
            description = """
                             Welcome to the UFO Sightings API!
                            \s
                             Available Resources:
                            \s
                             Sightings (/sightings):
                             GET /sightings and GET /sightings/{id}: Public, no authentication required.
                             POST, PUT, DELETE on /sightings: Require authentication.
                            \s
                             Shapes (/shapes):
                             Read-only (GET only), public.
                            \s
                             Locations (/locations):
                             Read-only (GET only), public.
                            \s
                             Auth:
                             POST /auth/register: Register a new user.
                             POST /auth/login: Obtain a JWT token.
                            \s
                             Note:\s
                             Only POST, PUT, and DELETE requests to /sightings require authentication.\s
                             All other endpoints are public.
                    \s""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "API landing page with resource overview",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "Welcome to the UFO Sightings API!",
                                                      "resources": [
                                                        {
                                                          "name": "Sightings",
                                                          "url": "/sightings",
                                                          "description": "GET is public. POST, PUT, DELETE require authentication."
                                                        },
                                                        {
                                                          "name": "Shapes",
                                                          "url": "/shapes",
                                                          "description": "Read-only access (GET only). No authentication required."
                                                        },
                                                        {
                                                          "name": "Locations",
                                                          "url": "/locations",
                                                          "description": "Read-only access (GET only). No authentication required."
                                                        },
                                                        {
                                                          "name": "Register",
                                                          "url": "/auth/register",
                                                          "description": "Register a new user."
                                                        },
                                                        {
                                                          "name": "Login",
                                                          "url": "/auth/login",
                                                          "description": "Obtain a JWT token for authentication."
                                                        }
                                                      ]
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public Map<String, Object> landingPage() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Welcome to the UFO Sightings API!");
        List<Map<String, String>> resources = List.of(
                Map.of(
                        "name", "Sightings",
                        "url", "/sightings",
                        "description", "GET is public. POST, PUT, DELETE require authentication."
                ),
                Map.of(
                        "name", "Shapes",
                        "url", "/shapes",
                        "description", "Read-only access (GET only). No authentication required."
                ),
                Map.of(
                        "name", "Locations",
                        "url", "/locations",
                        "description", "Read-only access (GET only). No authentication required."
                ),
                Map.of(
                        "name", "Register",
                        "url", "/auth/register",
                        "description", "Register a new user."
                ),
                Map.of(
                        "name", "Login",
                        "url", "/auth/login",
                        "description", "Obtain a JWT token for authentication."
                )
        );
        response.put("resources", resources);
        return response;
    }
}