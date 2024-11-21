package org.example.course2024.exception;

public class MasterNotThisPrice extends RuntimeException {
    public MasterNotThisPrice(String message) {
        super(message);
    }
}
