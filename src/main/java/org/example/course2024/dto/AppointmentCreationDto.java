package org.example.course2024.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.course2024.entity.Appointment;
import org.example.course2024.enums.StatusAppoint;

import java.io.Serializable;

/**
 * DTO for {@link Appointment}
 */
public record AppointmentCreationDto(@NotNull @Positive Long customerId, @NotNull @Positive Long scheduleId, @NotNull @Positive Long masterId, @NotNull StatusAppoint status,
                                     Long priceId) implements Serializable {
  }