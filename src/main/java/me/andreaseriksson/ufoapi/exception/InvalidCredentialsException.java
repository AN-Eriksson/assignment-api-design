package me.andreaseriksson.ufoapi.exception;

/**
 * Exception thrown when user authentication fails due to invalid credentials.
 *
 * Used to signal login failures when the provided username/email or password is incorrect.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
