package org.example.course2024.exception;

public class ScheduleAlreadyBusyException extends RuntimeException {
    public ScheduleAlreadyBusyException(String message) {
        super(message);
    }
}
