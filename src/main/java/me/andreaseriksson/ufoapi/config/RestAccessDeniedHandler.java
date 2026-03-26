package me.andreaseriksson.ufoapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Filter that authenticates incoming HTTP requests using JWT tokens.
 *
 * This filter checks for an Authorization header with a Bearer token.
 * If a valid JWT is found, it extracts the username and sets the authentication
 * in the Spring Security context for the current request.
 *
 * Used for stateless authentication in the API.
 *
 * Depends on JwtService for token validation and username extraction.
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean unauthenticated = (authentication == null || !authentication.isAuthenticated());

        HttpStatus status = unauthenticated ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN;
        String message = unauthenticated ? "Authentication required or token is invalid" : "Access denied";

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
