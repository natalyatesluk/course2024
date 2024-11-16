package org.example.course2024.dto;

import jakarta.validation.constraints.NotNull;
import org.example.course2024.enums.StatusAppoint;

import java.io.Serializable;

/**
 * DTO for {@link org.example.course2024.entity.Appointment}
 */
public record AppointmentCreationDto(Long customerId, Long scheduleId, Long masterId, @NotNull StatusAppoint status) implements Serializable {
  }