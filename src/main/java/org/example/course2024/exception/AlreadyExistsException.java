package org.example.course2024.exception;

public class AlreadyExistsException extends RuntimeException  {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
