package org.example.course2024.dto;

import org.example.course2024.enums.PartBody;
import org.example.course2024.enums.StatusAppoint;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.course2024.entity.Appointment}
 */
public record AppointmentDto(Long id, Long customerId, String customerSurname, String customerPhone, PartBody customerPartBody, LocalDateTime scheduleDate, Long masterId, String masterSurname, String masterPhone, StatusAppoint status) implements Serializable {
  }