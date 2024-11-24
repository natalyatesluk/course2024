package org.example.course2024.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.course2024.entity.Appointment;
import org.example.course2024.enums.PartBody;
import org.example.course2024.enums.StatusAppoint;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link Appointment}
 */
public record AppointmentDto(Long id, StatusAppoint status, String customerSurname, String customerPhone,
                             String customerEmail, PartBody customerPartBody,
                             String masterSurname, String masterPhone, String masterEmail,
                             String priceSize, BigDecimal priceBasePrice, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate scheduleDate,
                             @JsonFormat(pattern = "HH:mm") LocalTime scheduleTime) implements Serializable {
  }