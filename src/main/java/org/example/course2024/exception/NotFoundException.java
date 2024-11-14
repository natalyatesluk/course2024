package org.example.course2024.exception;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundException extends EntityNotFoundException {
    public NotFoundException(String message) {
        super(message);
    }
}
