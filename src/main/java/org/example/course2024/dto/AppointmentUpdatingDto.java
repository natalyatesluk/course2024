package org.example.course2024.dto;

import jakarta.validation.constraints.Positive;
import org.example.course2024.enums.StatusAppoint;

import java.io.Serializable;

/**
 * DTO for {@link org.example.course2024.entity.Appointment}
 */
public record AppointmentUpdatingDto(@Positive Long id, Long customerId, Long scheduleId, Long masterId,
                                     StatusAppoint status) implements Serializable {
}