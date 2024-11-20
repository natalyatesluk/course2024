package org.example.course2024.dto;

import org.example.course2024.enums.PartBody;
import org.example.course2024.enums.StatusAppoint;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.course2024.entity.Appointment}
 */
public record AppointmentDto(Long id, StatusAppoint status, String customerSurname, String customerPhone,
                             String customerEmail, PartBody customerPartBody, LocalDateTime scheduleDate,
                             String masterSurname, String masterPhone, String masterEmail) implements Serializable {
  }