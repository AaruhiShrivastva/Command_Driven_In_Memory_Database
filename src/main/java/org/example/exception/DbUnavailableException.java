package org.example.exception;

public class DbUnavailableException extends RuntimeException {
    public DbUnavailableException(String message) {
        super(message);
    }
}
