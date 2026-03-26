package me.andreaseriksson.ufoapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the UFO Sightings API Spring Boot application.
 *
 * Configures and launches the application, and sets up the OpenAPI definition
 * for API documentation.
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "UFO Sightings API",
		version = "1.0",
		description = "API for querying UFO sighting data"
))
public class WebapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebapiApplication.class, args);
	}

}
