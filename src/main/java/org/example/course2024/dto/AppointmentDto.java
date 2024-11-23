package org.example.course2024.dto;

import org.example.course2024.enums.PartBody;
import org.example.course2024.enums.StatusAppoint;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO for {@link org.example.course2024.entity.Appointment}
 */
public record AppointmentDto(Long id, StatusAppoint status, String customerSurname, String customerPhone,
                             String customerEmail, PartBody customerPartBody,
                             String masterSurname, String masterPhone, String masterEmail,
                             String priceSize, BigDecimal priceBasePrice, LocalDate scheduleDate,
                             LocalTime scheduleTime) implements Serializable {
  }