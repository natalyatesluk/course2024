package org.example.course2024.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import org.example.course2024.enums.StatusAppoint;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.course2024.entity.Appointment}
 */
public record AppointmentUpdatingDto(@Positive Long id, Long customerId, Long scheduleId, @FutureOrPresent LocalDateTime localDateTime, Long masterId,
                                     StatusAppoint status) implements Serializable {
}